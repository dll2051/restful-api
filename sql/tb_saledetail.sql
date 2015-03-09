

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `tb_saledetail`
-- ----------------------------
DROP TABLE IF EXISTS `tb_saledetail`;
CREATE TABLE `tb_saledetail` (
  `id` char(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '主键',
  `title` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '标题',
  `detail` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '明细',
  `status` int(1) DEFAULT '0' COMMENT '是否处理状态',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入库时间',
  `img` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '图片链接地址',
  `url` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '商品获取链接地址',
  `source` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '打折信息来源',
  `mallname` char(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '打折所在商城',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='商品原始表';

-- ----------------------------
--  Records of `tb_saledetail`
-- ----------------------------


SET FOREIGN_KEY_CHECKS = 1;
