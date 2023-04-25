### OCR模块
1. 上传文件：使用`@RequestParam("file") MultipartFile file`进行接受
2. `file.getContentType()`可以获取文件的类型，如果是pdf或者docx格式那么先转png，然后进行base64编码
    -   问题1：使用的`poi`包更新变动很大，但是对应的`pdf`包很久没有变动，内部的依赖出现问题。
3. 此后经由移动云ocr sdk进行识别