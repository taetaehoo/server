package Network;
import java.io.*;
import java.net.*;
import java.util.*;

public class CalcServerEx {
    private static String sortArray(String exp) {
        StringTokenizer st = new StringTokenizer(exp, " ");
        StringBuilder sb = new StringBuilder();
        if (st.countTokens() != 5) return "error";
        int [] arr = new int[5];

        for (int i = 0; i < 5; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        Arrays.sort(arr);
        for (int i : arr) {
            sb.append(i+ " ");
        }
        return sb.toString();
    }//정렬 코드 + 들어온 데이터 가공 후 재 가공

    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        ServerSocket listener = null;
        Socket socket = null;
        try {
            listener = new ServerSocket(9999); // 서버 소켓 생성
            System.out.println("연결을 기다리고 있습니다.....");
            socket = listener.accept(); // 클라이언트로부터 연결 요청 대기
            System.out.println("연결되었습니다.");
            in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String inputMessage = in.readLine();//client로 부터 응답 받기
                if (inputMessage.equalsIgnoreCase("bye")) {
                    System.out.println("클라이언트에서 연결을 종료하였음");
                    break; // "bye"를 받으면 연결 종료
                }
                System.out.println(inputMessage); // 받은 메시지를 화면에 출력
                String res = sortArray(inputMessage);//받은 메시지를 정렬 후 다시 가공
                out.write(res + "\n"); // 계산 결과 문자열 전송
                out.flush();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if(socket != null) socket.close(); // 통신용 소켓 닫기
                if(listener != null) listener.close(); // 서버 소켓 닫기
            } catch (IOException e) {
                System.out.println("클라이언트와 채팅 중 오류가 발생했습니다.");
            }
        }
    }
}
