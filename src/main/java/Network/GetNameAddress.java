package Network;
import java.io.*;
import java.net.*;
public class GetNameAddress
{
    public static void main(String args[]){
        InetAddress addr = null;
        InetAddress[] addrArr = null;
        String hostname;
        BufferedReader br;
        System.out.println("호스트 이름 또는 IP 주소를 입력하세요");
        br = new BufferedReader(new InputStreamReader(System.in));

        try{
            if((hostname = br.readLine()) != null){
                addr = InetAddress.getByName(hostname); // 도메인 이름으로 InetAddress 객체 생성
                System.out.println("호스트 이름은 "+addr.getHostName());
                System.out.println("IP 주소는 "+addr.getHostAddress());

                addrArr = InetAddress.getAllByName(hostname); // 도메인 이름으로 InetAddress 배열 객체 생성
                for(int i =0; i < addrArr.length;i++){
                    System.out.println("IP 목록["+i+"] : " + addrArr[i]);
                }

            }
            InetAddress laddr = InetAddress.getLocalHost();
            System.out.println("로컬 호스트 이름은 "+laddr.getHostName());
            System.out.println("로컬 IP 주소는 "+laddr.getHostAddress());
        }catch(UnknownHostException e){
            System.out.println(e);
        }catch(IOException e){
            System.out.println(e);
        }
    }
}