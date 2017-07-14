require(['md5',"jquery", "bootstrap", 'pm', "bootstrapvalidator"], function (md5) {

    $(document).ready(function () {
        //登陆页面跳转到注册,关闭模态框
        $(document).on('click', '#registerLink', function () {
                $('#loginModal').modal('hide');
                $('#registerModal').modal('show')
            })
            //发送验证码
        $('#authCodeBox').click(function(){
            $(this).attr('src', '/WiseAuth/AuthImageServlet?timeStamp=' + Date.now());
        });
        $('#close').click(function(){
            $('#getCode').prop('disabled', true).removeClass('btn-success');
        });

        var wait = 60;
        $('#getCode').on('click',function () {
            var phone = $("#phonenum").val();
            var code = $("#authCode").val();
            var o = $(this);
            checkTime(o);

            function checkTime(o) {
                if (phone && code) {
                    if (wait == 60) {
                        $.ajax({
                            type: "GET",
                            url: " /Public/Phone/SendCode/" + phone + '/' + code,
                            dataType: "json",
                            async: false,
                            success: function (data) {
                                if (data.success) {
                                    showAlert(data.msg,'success');
                                    wait--;
                                    checkTime(o);
                                }else{
                                    showAlert(data.msg,'danger')
                                }
                            },
                            error: function (e) {
                                showAlert('未知错误', 'danger');
                                return false;
                            }
                        })
                    } else if (wait == 0) {
                        o.attr('disabled', false);
                        o.html("获取手机验证码");
                        wait = 60;
                    } else {
                        o.attr('disabled', true);
                        o.html(wait + "秒后可重新发送");
                        wait--;
                        setTimeout(function () {
                            checkTime(o)
                        }, 1000)
                    }
                } else {
                    return false;
                }
            }
            return false;
        })

        $('#registerForm').attr('action', '/Login/register');

        $('#registerForm').bootstrapValidator({
                message: 'This value is not valid',
                excluded: [':disabled'],
                feedbackIcons: {
                    valid: '',
                    invalid: '',
                    validating: ''
                },
                fields: {
                    username: {
                        validators: {
                            notEmpty: {
                                message: '用户名不能为空'
                            },
                            regexp: {
                                regexp: /^[a-zA-Z][a-zA-Z0-9]{7,19}$/i,
                                message: '8-20个英文和数字组合，首字母必须为英文字母，特殊符号不可用'
                            }

                        }
                    },
                    password: {
                        validators: {
                            notEmpty: {
                                message: '密码不能为空'
                            },
                            stringLength: {
                                min: 8,
                                message: '密码不少于8个字符'
                            },
                            callback: {
                                message: '密码需包含数字和字母',
                                callback: function(value, validator, $field) {
                                    var rn = /\d+/g; //数字测试
                                    var ra = /[a-zA-Z]/g; //字母测试
                                    return rn.test(value) && ra.test(value);
                                }
                            }
                            
                        }
                    },
                    confirmpassword: {
                        validators: {
                            notEmpty: {
                                message: '确认密码不能为空'
                            },
                            identical: {
                                field: 'password',
                                message: '确认密码与密码不一致'
                            }

                        }
                    },
                    phone: {
                        validators: {
                            notEmpty: {
                                message: '手机号码不能为空'
                            },
                            regexp: {
                                regexp: /^1[0-9]{10}$/,
                                message: '请输入合法的11位手机号码'
                            }
                        }
                    },
                    authCode: {
                        validators: {
                            notEmpty: {
                                message: '验证码不能为空'
                            },
                            stringLength: {
                                min: 6,
                                max: 6,
                                message: '验证码错误'
                            }
                        }
                    },
                    code: {
                        validators: {
                            notEmpty: {
                                message: '校验码不能为空'
                            },
                            stringLength: {
                                min: 6,
                                max: 6,
                                message: '校验码错误'
                            }
                        }
                    }
                }
            })
            .on('success.form.bv', function (e) {
                // Prevent form submission
                e.preventDefault();

                // Get the form instance
                var $form = $(e.target),
                    validator = $form.data('bootstrapValidator'),
                    submitButton = validator.getSubmitButton();

                // 转义特殊字符，防止js注入
                checkJsInject();
                //密码加密
                $('#registerpsd').val(md5($('#registerpsd').val()));
                $('#registerCfmpsd').val(md5($('#registerCfmpsd').val()));

                // 注册接口
                $.ajax({
                    method: "POST",
                    url: '/Login/register',
                    data: $("#registerForm").serialize(),
                    //dataType: "json",
                    async: false,
                    success: function (result) {
                        if (result.success) {
                            //关闭模态框
                            $("#registerModal").modal("hide");
                            showAlert(result.msg,'success');
                            window.location.reload();
                        } else {
                            showAlert(result.msg, 'danger');
                            $('#registerpsd').val('');
                            $('#registerCfmpsd').val('');
                        }
                        //重置表格
                        $('#registerForm').data('bootstrapValidator').resetForm();
                    },
                    error: function () {
                        showAlert('提交失败', 'danger');
                    }
                });
            })
            .on('error.field.bv', function (e, data) {
                if (!data.bv.isValid()) {
                    // The postal code is not valid
                    $('#getCode').prop('disabled', true).removeClass('btn-success');
                }
            })
            .on('success.field.bv', function (e, data) {
                var phone = data.bv.getFieldElements('phone').val();
                var code = data.bv.getFieldElements('authCode').val();
                if (phone && code) {
                      // The postal code is valid
                    $('#getCode').prop('disabled', false).addClass('btn-success');
                }
            });

        $('#registerModal').on('show.bs.modal', function () {
            $('#registerForm').bootstrapValidator('resetForm', true);
        });
        $('#registerModal').on('hide.bs.modal', function () {
            $('#registerForm').bootstrapValidator('resetForm', true);
        });

        ////注册按钮调用接口
        //$('#registerBtn').click(function () {
        //    // 转义特殊字符，防止js注入
        //    checkJsInject();
        //    //密码加密
        //    $('#registerpsd').val(md5($('#registerpsd').val()));
        //    $('#registerCfmpsd').val(md5($('#registerCfmpsd').val()));
        //
        //    // 注册接口
        //    $.ajax({
        //        method: "POST",
        //        url: '/Login/register',
        //        data: $("#registerForm").serialize(),
        //        //dataType: "json",
        //        async: false,
        //        success: function (result) {
        //            if (result.success) {
        //                //关闭模态框
        //                $("#registerModal").modal("hide");
        //                showAlert(result.msg,'success');
        //                window.location.reload();
        //            } else {
        //                showAlert(result.msg, 'danger');
        //                $('#registerpsd').val('');
        //                $('#registerCfmpsd').val('');
        //            }
        //            //重置表格
        //            $('#registerForm').data('bootstrapValidator').resetForm();
        //        },
        //        error: function () {
        //            showAlert('提交失败', 'danger');
        //        }
        //    });
        //});

    });

})