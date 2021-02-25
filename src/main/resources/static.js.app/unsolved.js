let unsolved = {
    init: function() {
        let _this = this;
        $('.form-group').on('change', function() {
            _this.update();
        })
    },
    update: function() {
        let data = {
          tierFilter: $('input[name="tier"]:checked').val()
        };
        let param = {

        }
        let pathname = $(location).attr('pathname');
        $.ajax({
            type: "GET",
            url: pathname,
            dataType: 'json',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        })
    }
}

unsolved.init();