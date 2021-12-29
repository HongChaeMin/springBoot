package org.zerock.board.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.QBoard;
import org.zerock.board.entity.QMember;
import org.zerock.board.entity.QReply;

import java.util.List;
import java.util.stream.Collectors;

@Log4j2
public class SearchBoardRepositoryImpl extends QuerydslRepositorySupport implements SearchBoardRepository {

    public SearchBoardRepositoryImpl() {
        super(Board.class);
    }

    @Override
    public Board search1() {
        log.info("search1-----------------------");

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(board);

        // select one
        // jpqlQuery.select(board).where(board.bno.eq(1L));

        // left join & on
        // 여러 객체를 가져옴
        // jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        // Tuple 객체 : 각각의 데이터를 추출하는 경우에 사용
        jpqlQuery.leftJoin(member).on(board.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.board.eq(board));

        JPQLQuery<Tuple> tupleJPQLQuery = jpqlQuery.select(board, member.email, reply.count());
        tupleJPQLQuery.groupBy(board);

        log.info("---------------------------");
        log.info(tupleJPQLQuery);
        log.info("---------------------------");

        List<Tuple> result = tupleJPQLQuery.fetch();

        log.info(result);

        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage........................");
        // https://querydsl.com/static/querydsl/4.0.9/apidocs/overview-summary.html

        QBoard qBoard = QBoard.board;
        QReply qReply = QReply.reply;
        QMember qMember = QMember.member;

        JPQLQuery<Board> jpqlQuery = from(qBoard);
        jpqlQuery.leftJoin(qMember).on(qBoard.writer.eq(qMember));
        jpqlQuery.leftJoin(qReply).on(qReply.board.eq(qBoard));

        // SELECT b, w, count(r) FROM Board b
        // LEFT JOIN b.writer w LEFT JOIN Reply r ON r.board = b
        JPQLQuery<Tuple> tupleJPQLQuery = jpqlQuery.select(qBoard, qMember.email, qReply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression booleanExpression = qBoard.bno.gt(0L);

        booleanBuilder.and(booleanExpression);

        if (type != null) {
            String[] typeArr = type.split("");

            BooleanBuilder conditionBuilder = new BooleanBuilder();

            for (String t : typeArr) {
                switch (t) {
                    case "t" :
                        conditionBuilder.or(qBoard.title.contains(keyword));
                        break;
                    case "w" :
                        conditionBuilder.or(qMember.email.contains(keyword));
                        break;
                    case "c" :
                        conditionBuilder.or(qBoard.content.contains(keyword));
                        break;
                }
                booleanBuilder.and(conditionBuilder);
            }

            tupleJPQLQuery.where(booleanBuilder);

            // order by
            Sort sort = pageable.getSort();

            // 직접 코드 처리
            // tuple.orderBy(board.bno.desc());

            sort.stream().forEach(order -> {
                Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                String prop = order.getProperty();

                PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
                tupleJPQLQuery.orderBy(new OrderSpecifier(direction, orderByExpression.get(prop)));
            });

            tupleJPQLQuery.groupBy(qBoard);

            // page 처리
            tupleJPQLQuery.offset(pageable.getOffset());
            tupleJPQLQuery.limit(pageable.getPageSize());

            List<Tuple> result = tupleJPQLQuery.fetch();

            log.info(result);

            long count = tupleJPQLQuery.fetchCount();

            return new PageImpl<Object[]>(result.stream().map(t -> t.toArray()).collect(Collectors.toList()), pageable, count);
        }

        return null;
    }
}
