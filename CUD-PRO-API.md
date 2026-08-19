1、简介：此处介绍中台版本跟互联网版本合并后核心API说明（使用@Autowired 注入对应接口即可使用
   （注：所有接口方法均携带 String... args 扩展参数供业务系统使用

2、用户API接口：cud-pro-api/src/main/java/com/cgnpc/pro/api/ICudUserService.java
  
  2.1、核心方法：
       
       /**********************************
         * 用途说明:从上下文获取当前登录人用户工号
         * 返回值示例:  pxmwrya
         ***********************************/
         String getUser();
    
        /**********************************
         * 用途说明:从上下文获取当前登录人用户姓名
         * 返回值示例: 阮洋
         ***********************************/
        String getUserRealName();
    
        /**********************************
         * 用途说明: 获取人员信息
         * 参数说明 userId 用户工号
         * 返回值说明: CudUserInfoVO 用户基本信息对象
         ***********************************/
        CudUserInfoVO getUsersInfo(String userId);
    
        /**********************************
         * 用途说明:从上下文获取当前登录人名称
         * 返回值示例: [pxmwrya]阮洋
         ***********************************/
        String getNowUser();
    
        /**********************************
         * 用途说明: 获取用户姓名
         * 参数说明 userId 用户工号
         * 返回值示例: 阮洋
         ***********************************/
        String getUserName(String userId);
    
        /**********************************
         * 用途说明: 获取用户姓名
         * 参数说明 userId 用户工号
         * 返回值说明: [pxmwrya]阮洋
         ***********************************/
        String getUserInfo(String userId);
    
        /**********************************
         * 用途说明: 获取用户电话
         * 参数说明 userId 用户工号
         * 返回值说明: 用户电话
         ***********************************/
        String getUserPhoneNum(String userId);
    
        /**********************************
         * 用途说明: 获取多个用户的信息
         * 参数说明 userIds 逗号分割  p123456,p234567
         * 返回值说明: 用户基本信息对象
         ***********************************/
        List<UserModel> getUsersByUserCenter(String userIds);
    
        /***********************************
         * 用途说明: 根据部门ID获取当前部门下人员信息结果集
         * 参数说明 HrcenterDto
         * 返回值说明:  用户基本信息对象
         ***********************************/
        List<CudUserCenterVO> getStaffsByOrgId(HrcenterDto hrcenterDto);
    
    
        /***********************************
         * 用途说明: 根据查询条件查询员工列表（选人选部门组件）
         * 参数说明 HrcenterDto
         * 返回值说明: 用户基本信息对象
         ***********************************/
        List<CudUserInfoVO> getUserList(HrcenterDto dto);

3、部门API接口：cud-pro-api/src/main/java/com/cgnpc/pro/api/ICudDeptService.java
  
  3.1、核心方法：

         /***********************************
          * 用途说明: 根据部门ID获取当前部门下人员信息结果集
          * 参数说明 orgId 部门id
          * 返回值说明: 用户基本信息对象
          ***********************************/
         List<UserModel> getStaffsByOrgId(String orgId);
     
         /***********************************
          * 用途说明: 根据部门ID与查询类型（1、正职，2、副职）查询部门正职、副职
          * 参数说明 orgId 部门ID , type （1、正职，2、副职）
          * 返回值说明: 用户基本信息对象
          ***********************************/
         List<UserModel> getDeptLeader(String orgId, String type);
     
         /***********************************
          * 用途说明: 根据部门ID与查询类型（3、秘书，4、预算协调员） 查询部门秘书、预算协调员
          * 参数说明 orgId 部门ID , type（3、秘书，4、预算协调员）
          * 返回值说明: 用户基本信息对象
          ***********************************/
         List<UserModel> getDeptPost(String orgId, String type);
     
     
         /***********************************
          * 用途说明: 根据部门id查询人员列表信息（选部门组件）
          * 参数说明 HrcenterDto
          * 返回值说明: 部门基本信息对象
          ***********************************/
         List<CudDeptCenterVO> getStaffsByOrgId(HrcenterDto dto);
         
4、权限菜单API接口：cud-pro-api/src/main/java/com/cgnpc/pro/api/IPermissionService.java
  
  4.1、核心方法：（注：解耦版本方法args可选参数需携带当前应用id 
  
      /**
       * 根据用户ID查询菜单列表
       *
       * @param userId 用户ID
       * @return 菜单基本信息列表
       */
      Set<Menu> selectMenus(String userId, String... args);
  
      /**
       * 根据用户ID查询角色
       *
       * @param userId 用户ID
       * @return 角色列表
       */
      Set<String> selectRoles(String userId, String... args);
  
      /**
       * 根据用户ID查询权限
       *
       * @param userId 用户ID
       * @return 权限列表
       */
      Set<String> selectPermissions(String userId, String... args);
      
         
5、邮件API接口：cud-pro-api/src/main/java/com/cgnpc/pro/api/ICudEmailService.java
  
  5.1、核心方法：
  
      /**
       * 发送邮件
       *
       * @param alowUser  自己账号
       * @param cc        抄送人
       * @param content   内容
       * @param sendStyle 格式
       * @param sendTo    接收人
       * @param subject   主题
       * @return Boolean 发送结果
       */
      Boolean send(String alowUser, String[] cc, String content, String sendStyle, String[] sendTo, String subject);
      
      
         
6、短信API接口：cud-pro-api/src/main/java/com/cgnpc/pro/api/ICudSmsService.java
  
  6.1、核心方法：
  
       /**
        * @Description 短信发送方法
        * @Date 9:59 2023/12/11
        * @Param [content 发送内容, phoneNos 接收人手机号, requestStaffNo 发送人, args]
        * @return Boolean 发送结果
        **/
       Boolean send(String content, String phoneNos, String requestStaffNo);
       
       
         
6、文件API接口：cud-pro-api/src/main/java/com/cgnpc/pro/api/ICudFileService.java
  
  6.1、核心方法：
  
    /**********************************
       * 用途说明: 上传文件
       * 参数说明 file
       * 返回值说明: RespUploadFileDto 文件上传结果对象
       ***********************************/
      RespUploadFileDto upfile(MultipartFile file) throws Exception;
  
      /**********************************
       * 用途说明: 上传大于50MB文件
       * 参数说明 file
       * 返回值说明: RespUploadFileByChunkDto 文件上传结果对象
       ***********************************/
      RespUploadFileByChunkDto upfileByChunk(MultipartFile file) throws Exception;
  
      /**********************************
       * 用途说明: 下载文件
       * 参数说明 file
       * 返回值说明: byte
       ***********************************/
      byte[] downloadFile(HashMap<String,Object> map) throws Exception;
  
      /**********************************
       * 用途说明: 下载限定文档文件
       * 参数说明 file
       * 返回值说明:
       ***********************************/
      byte[] downloadPartFileBytes(HashMap<String,Object> map) throws Exception;
  
      /**
       * @Author P629041
       * @Description  获取临时文件地址
       * @Date 17:44 2023/5/4
       * @Param [respUploadUrlsDto]
       * @return FileUrlVO
       **/
      List<FileUrlVO> getUrls(RespUploadUrlsDto respUploadUrlsDto);
  
      /**
       * @Author P629041
       * @Description  批量删除文件 （物理删除
       * @Date 17:44 2023/5/4
       * @Param [respUploadUrlsDto]
       * @return DelFileResultVO
       **/
      List<DelFileResultVO> physicalDelFile(RespUploadUrlsDto respUploadUrlsDto);
  