var common = common || (function () {

    var baseContxt = "/";

    $(window).on("load", function () {
        // Run code
        var status = $("#pagestatus").val();
        var message = $("#pagemsg").val();
        if (status == 200)
            toastr.success(message);
        else if (status == 300)
            toastr.error(message);
        //else
        //toastr.warning(message);  


    });

    return {

        init: function (_basecontext) {
            baseContxt = _basecontext;
        }

    };

}());