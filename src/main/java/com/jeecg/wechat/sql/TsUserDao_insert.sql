INSERT  INTO
    t_s_user
      ( 
      id                            
      ,email                          
      ,mobilePhone                           
      ,officePhone                            
      ,signatureFile                       
      ,update_name                         
      ,update_date                                         
      ,update_by                      
      ,create_name                         
      ,create_date                       
      ,create_by                         
      ,portrait                                         
      ,imsign                      
      ,dev_flag
	  ,user_type                                         
      ,person_type                      
      ,sex                         
      ,emp_no                       
      ,citizen_no                         
      ,fax                                         
      ,address                      
      ,post
	  ,memo
      ,wxopenid
      ,province
      ,city
      ,country
      ) 
values
      (
      :t_s_user.id                            
      ,:t_s_user.email                          
      ,:t_s_user.mobilePhone                           
      ,:t_s_user.officePhone                            
      ,:t_s_user.signatureFile                       
      ,:t_s_user.update_name                         
      ,:t_s_user.update_date                                         
      ,:t_s_user.update_by                      
      ,:t_s_user.create_name                         
      ,:t_s_user.create_date                       
      ,:t_s_user.create_by                         
      ,:t_s_user.portrait                                         
      ,:t_s_user.imsign                      
      ,:t_s_user.dev_flag
	  ,:t_s_user.user_type                                         
      ,:t_s_user.person_type                      
      ,:t_s_user.sex                         
      ,:t_s_user.emp_no                       
      ,:t_s_user.citizen_no                         
      ,:t_s_user.fax                                         
      ,:t_s_user.address                      
      ,:t_s_user.post
	  ,:t_s_user.memo
      ,:t_s_user.wxopenid
      ,:t_s_user.province
      ,:t_s_user.city
      ,:t_s_user.country                    
      )