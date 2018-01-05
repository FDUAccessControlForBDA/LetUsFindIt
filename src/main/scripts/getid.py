# coding = utf-8
import requests
import codecs
import time

def generateIdCard(region):
    if len(region)!=6:
        return False
    birthdate='19960526'
    sex='207'
    ret=region+birthdate+sex
    PARITYBIT=('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2')
    POWER_LIST=(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2)
    power=0
    for i in range(17):
        power +=int(ret[i]) * POWER_LIST[i]
    ret+=PARITYBIT[power % 11];
    return ret;

# 打开一个文件
fo = codecs.open("id.txt", "w","utf-8")

payload={'idcard':'','appkey':'1307ee261de8bbcf83830de89caae73f'}
for i in range(110000,670000):
     code = str(i)
     payload['idcard']=generateIdCard(code)
     r = requests.get('http://api.46644.com/idcard',params=payload)
     result=r.json()
     if result["address"] != "所属区域不详" :
         print(i)
         fo.write(code+" "+result["address"]+"\n")
    #  time.sleep(1)

# 关闭打开的文件
fo.close()