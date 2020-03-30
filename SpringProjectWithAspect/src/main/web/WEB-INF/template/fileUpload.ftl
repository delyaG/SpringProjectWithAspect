<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <script
            src="https://code.jquery.com/jquery-3.4.1.js"
            integrity="sha256-WpOohJOqMqqyKL9FccASB9O0KwACQJpFTUBLTYOVvVU="
            crossorigin="anonymous"></script>
    <title>Title</title>
    <script>
        function sendFile() {
            // данные для отправки
            let formData = new FormData();
            // забрал файл из input
            let files = ($('#file'))[0]['files'];
            // добавляю файл в formData
            [].forEach.call(files, function (file, i, files) {
                formData.append("file", file);
            });

            $.ajax({
                type: "POST",
                url: "http://localhost:8080/files",
                data: formData,
                processData: false,
                contentType: false
            })
                .done(function (response) {
                    alert(response)
                })
                .fail(function () {
                    alert('Error')
                });
        }
    </script>
</head>
<body>
<form action="/files" method="post" enctype="multipart/form-data">
<div>
    <input type="file" id="file" name="file" placeholder="FileName..."/>
    <button onclick="sendFile()">
        Upload file
    </button>
    <input type="hidden" id="file_hidden">
    <div class="filename"></div>
</div>
</form>
</body>
</html>