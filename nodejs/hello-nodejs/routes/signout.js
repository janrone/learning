var express = require('express');
var router = express.Router();

var checkLogin = require('../middlewares/check').checkLogin;

// GET /signout 登出
router.get('/', checkLogin, function(req, res, next) {
   //清空session 
   req.session.user = null;
   req.flash('success', '登出成功');
   res.redirect('/posts');
});

module.exports = router;


