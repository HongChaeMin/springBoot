$(document).ready(function (e) {

    let regex = new RegExp("(.*?)\.(exe|sh|zip|alz|tiff)$");
    let maxSize = 10485760;

    function checkExtension(fileName, fileSize) {
        if (fileSize >= maxSize) {
            alert("파일 사이즈를 초과하였습니다.");
            return false;
        }

        if (regex.test(fileName)) {
            alert("이미지 파일만 업로드할 수 있습니다.");
            return false;
        }
        return true;
    }

    function showResult(uploadResultArr) {
        let uploadUL = $(".uploadResult ul");

        let str = "";

        $(uploadResultArr).each(function (i, obj) {
            str += '<li data-name="' + obj.fileName + '" data-path="' + obj.folderPath + '" data-uuid="' + obj.uuid + '">';
            str += '    <div>';
            str += '        <button type="button" data-file="' + obj.imageURL + '" class="btn btn-outline-info btn-sm">X</button>';
            str += '        <img src="/display?fileName=' + obj.thumbnailURL + '">';
            str += '    </div>';
            str += '</li>';
        });
        uploadUL.append(str);
    }

    $(".custom-file-input").on("change", function () {
        // window는 파일 전체 경로로 들어옴
        // let fileName = $(this).val().split("\\").pop();

        let fileName = $(this).val();
        $(this).siblings(".custom-file-label").addClass("selected").html(fileName);

        let formData = new FormData();

        let inputFile = $(this);

        let files = inputFile[0].files;

        let appended = false;

        for (let i = 0; i < files.length; i++) {
            if (!checkExtension(files[i].name, files[i].size)) {
                // 파일이 여러개일 경우의 수
                appended = false;
                return false;
            }
            formData.append("uploadFiles", files[i]);
            appended = true;
        }

        // upload를 하지 않는다
        if (!appended) {return; }

        $.ajax({
            url: '/uploadFile',
            processData: false,
            contentType: false,
            data: formData,
            type: 'POST',
            dataType: 'JSON',
            success: function (result) {
                showResult(result);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                console.log(textStatus);
            }
        });

    });

    $(".uploadResult").on("click", "li button", function (e) {
        let target = $(this);
        let targetFile = target.data("file");
        let targetLI = $(this).closest("li");

        $.post("/deleteFile", {fileName: targetFile}, function (result) {
            if (result === true) {
                targetLI.remove();
            }
        });
    });

    $(".btn-primary").on("click", function (e) {
        e.preventDefault();

        let str = "";
        $(".uploadResult li").each(function (i, obj) {
            let target = $(obj);
            /*let movieImgParams = {
                "fileName" : target.data("name")
                , "uuid" : target.data("uuid")
                , "folderPath" : target.data("path")
            }

            console.log(movieImgParams);

            str += '<input type="hidden" name="imageDTOList[' + i + ']" value="' + movieImgParams + '">';*/

            str += '<input type="hidden" name="imageDTOList[' + i + '].fileName" value="' + target.data("name") + '">';
            str += '<input type="hidden" name="imageDTOList[' + i + '].folderPath" value="' + target.data("path") + '">';
            str += '<input type="hidden" name="imageDTOList[' + i + '].uuid" value="' + target.data("uuid") + '">';
        });
        $(".box").html(str);

        $("form").submit();
    });


});