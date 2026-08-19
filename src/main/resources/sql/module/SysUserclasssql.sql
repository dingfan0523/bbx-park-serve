-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${table.comment}', '3', '1', '/module/sysUserclass', 'C', '0', 'module:sysUserclass:view', '#', 'pxmwlin', '2019-05-14', 'pxmwlin', '2019-05-14', '${table.comment}菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${table.comment}查询', @parentId, '1',  '#',  'F', '0', 'module:${classname}:list',         '#', 'pxmwlin', '2019-05-14', 'pxmwlin', '2019-05-14', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${table.comment}新增', @parentId, '2',  '#',  'F', '0', 'module:${classname}:add',          '#', 'pxmwlin', '2019-05-14', 'pxmwlin', '2019-05-14', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${table.comment}修改', @parentId, '3',  '#',  'F', '0', 'module:${classname}:edit',         '#', 'pxmwlin', '2019-05-14', 'pxmwlin', '2019-05-14', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${table.comment}删除', @parentId, '4',  '#',  'F', '0', 'module:${classname}:remove',       '#', 'pxmwlin', '2019-05-14', 'pxmwlin', '2019-05-14', '');

insert into sys_menu  (menu_name, parent_id, order_num, url,menu_type, visible, perms, icon, create_by, create_time, update_by, update_time, remark)
values('${table.comment}保存', @parentId, '5',  '#',  'F', '0', 'module:${classname}:save',         '#', 'pxmwlin', '2019-05-14', 'pxmwlin', '2019-05-14', '');
