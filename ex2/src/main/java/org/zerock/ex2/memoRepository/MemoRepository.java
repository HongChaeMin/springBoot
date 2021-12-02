package org.zerock.ex2.memoRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.ex2.entity.Memo;

// JpaRepository 선언만으로 자동으로 빈 등록됨
public interface MemoRepository  extends JpaRepository<Memo, Long> {



}
