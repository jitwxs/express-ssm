/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50719
 Source Host           : localhost:3306
 Source Schema         : express

 Target Server Type    : MySQL
 Target Server Version : 50719
 File Encoding         : 65001

 Date: 11/06/2018 18:32:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for express
-- ----------------------------
DROP TABLE IF EXISTS `express`;
CREATE TABLE `express`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `tel` varchar(24) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '手机号码',
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '快递短信',
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '配送地址',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '订单备注',
  `status` int(11) NOT NULL COMMENT '订单状态',
  `staff` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送人员',
  `staff_remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '配送人员备注',
  `has_delete` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否删除（默认false）',
  `create_date` datetime(0) NOT NULL COMMENT '下单时间',
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_status`(`status`) USING BTREE,
  INDEX `fk_staff`(`staff`) USING BTREE,
  CONSTRAINT `fk_staff` FOREIGN KEY (`staff`) REFERENCES `sys_user` (`id`) ON DELETE SET NULL ON UPDATE SET NULL
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of express
-- ----------------------------
INSERT INTO `express` VALUES ('152441585858765', '张三', '14785321485', '【菜鸟驿站】您的10月18日韵达包裹等您很久了，请凭取货码（42518）尽快到金陵科技学院北区菜鸟驿站领取。\r\n', '南区10栋', '被拒收也要送！！', 4, 'fb13f057c76f4874915b44e2c406ce96', 'qqqq', 0, '2018-05-02 13:40:49', '2018-05-18 14:26:43');
INSERT INTO `express` VALUES ('152441585858766', '吴越', '15601457832', '您的10月23日顺丰快递等您很久了，请凭取货码（1466）尽快到北区菜鸟驿站领取。', '北区2栋', '不是本人快递', 3, 'fb13f057c76f4874915b44e2c406ce96', NULL, 0, '2018-05-13 13:40:49', '2018-05-18 14:26:48');
INSERT INTO `express` VALUES ('152441585858767', '李富贵', '15951478628', '您的10月23日天猫超市等您很久了，请凭取货码（6666）尽快到北区菜鸟驿站外领取。', '南区1栋', NULL, 4, 'ad0bfcd15f6142e0865715277cbcaf50', '电话无人接听', 0, '2018-05-02 13:40:49', '2018-05-18 14:31:02');
INSERT INTO `express` VALUES ('152441585858768', '张万平', '14785962146', '您的10月28日中通快递等您很久了，请凭取货码（7146）尽快到北区菜鸟驿站领取。', '南区4栋', NULL, 4, 'ad0bfcd15f6142e0865715277cbcaf50', '无人应答', 0, '2018-05-02 13:41:00', '2018-05-18 14:32:03');
INSERT INTO `express` VALUES ('152441585858769', '马化腾', '12945324851', '您的10月27日邮政快递等您很久了，请凭取货码（7）尽快到南区菜鸟驿站领取。', '北区5栋', NULL, 4, 'ad0bfcd15f6142e0865715277cbcaf50', '拉不拉', 0, '2018-05-02 13:41:02', '2018-05-18 14:32:44');
INSERT INTO `express` VALUES ('152441585858770', '牛龙', '18175201463', '您的10月27日京东快递等您很久了，请凭取货码（12）尽快到南区菜鸟驿站领取。', '南区3栋', '时间不急', 4, 'ad0bfcd15f6142e0865715277cbcaf50', '皮', 0, '2018-05-02 13:41:08', '2018-05-18 15:09:45');
INSERT INTO `express` VALUES ('152441585858771', '陈风', '14796521423', '您的10月27日天天快递等您很久了，请凭取货码（11147）尽快到北区菜鸟驿站领取。', '南区9栋', '', 2, 'ad0bfcd15f6142e0865715277cbcaf50', NULL, 0, '2018-05-02 13:41:10', '2018-05-18 15:06:54');
INSERT INTO `express` VALUES ('152441585858772', '杨慧', '14785236987', '【顺丰快递】货号547您的小米包裹已到南区洗衣房，请及时查收领取，有问题及时回电15050538555.十九点前务必来取啊！', '南区10栋', '晚上七点前', 1, NULL, NULL, 0, '2018-05-02 13:41:16', '2018-05-13 19:18:18');
INSERT INTO `express` VALUES ('152441585858773', '刘琳', '14796541472', '【菜鸟驿站】圆通包裹到金陵科技学院北区菜鸟驿站，请19:00前凭93502取，联系方式： m.tb.cn/E.Lj8Cq。', '北区02栋', '', 2, 'ad0bfcd15f6142e0865715277cbcaf50', NULL, 0, '2018-05-02 13:41:13', '2018-05-18 15:06:57');
INSERT INTO `express` VALUES ('152441585858774', '李泉', '12394313804', '【云喇叭】晟邦物流编号20你好，下午5点45准时到，新工，东大门外取件\\Z只等40分钟，请抓紧时间.谢谢。13092170696', '新区9栋601-1', '', 1, NULL, NULL, 0, '2018-05-02 13:41:19', '2018-05-18 14:23:25');
INSERT INTO `express` VALUES ('152441585858776', '王伟', '19741286542', '您的10月23日天猫超市等您很久了，请凭取货码（2333）尽快到南门外领取。', '北区3栋', '请早点送', 1, NULL, NULL, 0, '2018-05-02 13:41:23', NULL);
INSERT INTO `express` VALUES ('152441585858777', '徐敬', '13770416905', '【妈妈驿站】取货码M91,快递已到新工22栋106圆通爆仓八点前电话15950230722投诉电话051589884000', '3栋502-1', '', 1, NULL, NULL, 0, '2018-05-02 13:41:38', NULL);
INSERT INTO `express` VALUES ('152441585858778', '孔荣', '13589123206', '【云喇叭】晟邦物流编号97请立刻到东大门外凭编号取件，抓紧时间，可请同学代收不放代收点，13770280053', '23栋3单元606_1        孔荣笙', '帮我送上楼，我可以多给钱，谢谢！', 1, NULL, NULL, 0, '2018-05-02 13:41:26', NULL);
INSERT INTO `express` VALUES ('152441585858779', '孙程', '15381802575', '【云喇叭】中国邮政编号A204请22点前到新工男生宿舍14号楼学创文印EMS速取。当天不取，默认本人签收。尽快取走，方便自己处售后', '23栋606-1', '', 1, NULL, NULL, 0, '2018-05-02 13:41:40', NULL);
INSERT INTO `express` VALUES ('152441585858780', '司马衷', '11330693054', '【妈妈驿站】取货码M112,快递已到新工22栋106圆通爆仓十点前电话15950230722投诉电话051589884000', '没人就放客厅 请送上楼谢谢', '', 1, NULL, NULL, 0, '2018-05-02 13:41:28', NULL);
INSERT INTO `express` VALUES ('152441585858781', '王维', '18913376299', '【云喇叭】京东快递编号26，双十一爆仓，收到短信第一时间到22栋105京东派领取（当天件当天取（有问题回电17768222678', '三栋520-1', '送上楼', 1, NULL, NULL, 0, '2018-05-02 13:41:33', NULL);
INSERT INTO `express` VALUES ('152441585858783543', '张良', '12398643212', '【天天快递】您的运单668155863524,请于新工25幢小牛户外店内领取。拒签请拨17626632264。编号在此（64）麻烦务必今天领取。,如有问题请联系18551528821', '3栋', '', 1, NULL, NULL, 0, '2018-05-02 13:41:31', NULL);
INSERT INTO `express` VALUES ('152441585858784231', '快递员1', '13123561738', '【云喇叭】韵达速递编号D6，双十一爆满，请务必务必20点前到新工22栋108室取件，一定要取，非常感谢，18861997207', '3-502-1', '请送上楼谢谢 没人就放客厅', 1, NULL, NULL, 0, '2018-05-02 13:41:05', '2018-05-14 17:52:58');
INSERT INTO `express` VALUES ('995856581721690114', '徐文殊', '14795214863', '【菜鸟驿站】申通包裹到盐城工学院新校区20栋区菜鸟驿站，请当天19:30前凭1424取，联系方式： m.tb.cn/E.QnZ6i。', '3栋502-1', '送上楼', 3, '04663cac562d4585ace14e6f9a8044ee', NULL, 0, '2018-05-14 10:41:11', '2018-05-14 19:39:30');

-- ----------------------------
-- Table structure for express_payment
-- ----------------------------
DROP TABLE IF EXISTS `express_payment`;
CREATE TABLE `express_payment`  (
  `express_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单号',
  `status` int(11) NULL DEFAULT NULL COMMENT '支付状态',
  `type` int(11) NULL DEFAULT NULL COMMENT '支付方式',
  `online_payment` double NULL DEFAULT NULL COMMENT '线上付款金额',
  `online_payment_num` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线上支付第三方的流水号',
  `online_seller` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '线上收款方',
  `offline_payment` double NULL DEFAULT NULL COMMENT '线下支付金额',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_date` datetime(0) NOT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`express_id`) USING BTREE,
  INDEX `fk_payment_type`(`type`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '订单支付表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of express_payment
-- ----------------------------
INSERT INTO `express_payment` VALUES ('995856581721690114', NULL, 0, NULL, NULL, NULL, 5, NULL, '2018-05-14 10:41:54', '2018-05-14 19:39:30');
INSERT INTO `express_payment` VALUES ('995861583441702913', NULL, 0, NULL, NULL, NULL, 2.5, NULL, '2018-05-14 11:01:18', '2018-05-14 19:30:25');
INSERT INTO `express_payment` VALUES ('996276632522145794', NULL, 0, NULL, NULL, NULL, 5, NULL, '2018-05-15 14:30:34', NULL);
INSERT INTO `express_payment` VALUES ('997334660235247618', NULL, 0, NULL, NULL, NULL, 3, NULL, '2018-05-18 12:34:47', NULL);
INSERT INTO `express_payment` VALUES ('997336017193254914', NULL, 0, NULL, NULL, NULL, 2, NULL, '2018-05-18 12:40:11', NULL);

-- ----------------------------
-- Table structure for feedback
-- ----------------------------
DROP TABLE IF EXISTS `feedback`;
CREATE TABLE `feedback`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '姓名',
  `tel` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '联系电话',
  `type` int(1) NOT NULL COMMENT '反馈类型（1订单反馈；2意见反馈；3BUG反馈）',
  `message` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '反馈信息',
  `status` int(1) NULL DEFAULT 0 COMMENT '反馈状态。0：等待处理；1：处理完成',
  `staff_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '负责人',
  `result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '处理结果',
  `create_date` datetime(0) NOT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_staff_id`(`staff_id`) USING BTREE,
  CONSTRAINT `fk_staff_id` FOREIGN KEY (`staff_id`) REFERENCES `sys_user` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of feedback
-- ----------------------------
INSERT INTO `feedback` VALUES ('1b231b06cabe4a2e94e2a807a468312b', '李梦', '14785214695', 2, '希望下单时增加价格计算功能', 1, 'e652d4ac148a49329fc0f9da0f8531a3', '该功能将在近期上线，感谢您的反馈。', '2018-04-23 00:49:42', '2018-05-14 15:15:27');
INSERT INTO `feedback` VALUES ('679f0cade1ba4682a6fee12f3aa2119e', '张全蛋', '12367431229', 2, '希望接入在线支付', 0, NULL, '', '2018-04-23 00:49:44', NULL);
INSERT INTO `feedback` VALUES ('813a121c72fe4d1bb7452d6414094da4', '王伟', '19741286542', 1, '手机停机，请重新派送', 1, '04663cac562d4585ace14e6f9a8044ee', '已为您重新派送', '2018-04-23 00:49:40', '2018-05-14 14:10:51');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '登录名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `tel` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `sex` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '居住地址',
  `birthday` datetime(0) NULL DEFAULT NULL,
  `role_id` int(11) NOT NULL COMMENT '角色；0：admin；1：staff',
  `status` int(11) NOT NULL DEFAULT 0 COMMENT '状态；0：在职；1：冻结；2：离职',
  `create_date` datetime(0) NOT NULL,
  `update_date` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '职员表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('04663cac562d4585ace14e6f9a8044ee', '李四', 'b2d1f375db625e65a4a63ba0c7b29bdd738392cd83c38b8216c10c4d', '12345678910', 'male', '南区十栋', '2018-05-14 00:00:00', 1, 0, '2018-04-23 00:28:54', '2018-05-18 15:02:47');
INSERT INTO `sys_user` VALUES ('ad0bfcd15f6142e0865715277cbcaf50', '小红', 'b2d1f375db625e65a4a63ba0c7b29bdd738392cd83c38b8216c10c4d', '18943276592', 'female', '北区8栋A 503-2', '2018-05-01 00:00:00', 1, 2, '2018-04-23 00:29:22', '2018-05-18 15:02:44');
INSERT INTO `sys_user` VALUES ('e652d4ac148a49329fc0f9da0f8531a3', 'admin', 'b2d1f375db625e65a4a63ba0c7b29bdd738392cd83c38b8216c10c4d', '18168404329', 'male', NULL, '1997-11-13 00:27:08', 0, 0, '2018-04-23 00:26:20', '2018-05-14 15:55:28');
INSERT INTO `sys_user` VALUES ('fb13f057c76f4874915b44e2c406ce96', '张三', 'b2d1f375db625e65a4a63ba0c7b29bdd738392cd83c38b8216c10c4d', '138654285', 'male', '北区2栋B 301-3', '2007-03-15 00:00:00', 1, 2, '2018-04-23 00:28:21', '2018-05-18 15:02:44');

SET FOREIGN_KEY_CHECKS = 1;
