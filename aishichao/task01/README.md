# 1.��Ŀ����
  ����Ŀ��ubuntuϵͳ��,����golang������������docker�����У����ڲɼ������cpu���ڴ������ʣ�������shell�ն��ϲ�ѯ���ݡ�  
# 2.��Ŀ����
  ## (1).��װdocker
      curl -fsSL https://test.docker.com -o test-docker.sh  
      �����ʾΪ�ҵ�����curl,�谲װ��sudo apt-get install curl  
      sudo sh test-docker.sh  
  ## (2).��װddocker-compose  
      sudo curl -L "https://github.com/docker/compose/releases/download/v2.2.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose  
      sudo chmod +x /usr/local/bin/docker-compose  
  ## (3).���뵽��Ŀ��Ŀ¼��������Ŀ  
      sudo docker-compose up --build
      ��ʱ����ʾ�Ҳ���mysql�����������  
  ## (4).mysql����  
      �ҵ�mysql����id:sudo docker ps -a  
      ����mysql��sudo docker exec -it <ID> /bin/bash  
      ����task�⣺use tasks;  
      ������create table metric(  
                 id int AUTO_INCREMENT PRIMARY KEY,  
                 Metric varchar(255) not null,  
                 Endpoint varchar(255) not null,  
                 Timestamp bigint not null,  
                 Step bigint not null,  
                 Value double precision not null);  
  ## (5).��Ŀ�������  
    
# 3.��Ŀʹ��  
  (1).��װjq���ߣ�`sudo-apt-get install jq`</p>  
  (2).����Ŀ��Ŀ¼ִ��:��ӽű�ִ��Ȩ�ޣ�`chmod +x reader.sh`</p>   
  (3).����Ŀ��Ŀ¼ִ�У�./reader.sh ������ʾ���в�ѯ</p>  
      
