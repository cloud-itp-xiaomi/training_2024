# 1.��Ŀ����
  ����Ŀ��Ubuntu20.04ϵͳ��,����Golang������������Docker�����У����ڲɼ������cpu���ڴ�������ʣ�������Shell�ն��ϲ�ѯ���ݡ�  
# 2.��Ŀ����
  ## (1).��װDocker
      curl -fsSL https://test.docker.com -o test-docker.sh  
      �����ʾδ�ҵ�����curl,�谲װ��sudo apt-get install curl  
      sudo sh test-docker.sh  
  ## (2).��װDocker Compose  
      sudo curl -L "https://github.com/docker/compose/releases/download/v2.2.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose  
      sudo chmod +x /usr/local/bin/docker-compose  
  ## (3).���뵽��Ŀ��Ŀ¼��������Ŀ  
      sudo docker-compose up --build
      ��ʱ����ʾ�Ҳ���MySQL�����������  
  ## (4).MySQL����  
      �ҵ�MySQL����id:sudo docker ps -a  
      ����MySQL��sudo docker exec -it <Container_ID> /bin/bash  
      ����task�⣺use task;  
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
      
