package Network;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

public class Protocol {
    final static public int UNDEFINED = 0;
    final static public int T1_MANAGER_LOGIN_REQ = 1;
    final static public int T2_MANAGER_REGISTER_REQ = 2;
    final static public int T3_PROFESSOR_LOGIN_REQ = 3;
    final static public int T4_STUDENT_LOGIN_REQ = 4;
    final static public int T5_EXIT = 5;
    final static public int T6_MANAGER_LOGIN_RES = 6;
    final static public int T7_MANAGER_REGISTER_RES = 7;
    final static public int T8_PROFESSOR_LOGIN_RES = 8;
    final static public int T9_STUDENT_LOGIN_RES = 9;

    //CODE
    final static public int CD0_SUCCESS = 0;
    final static public int CD1_FAIL = 1;
    final static public int NOT_FOUND = 2;



    //T6_0
    final static public int T6_0_BACK_TO = 0;
    final static public int T6_0_CREATE_PROFESSOR_REQ = 1;
    final static public int T6_0_CREATE_STUDENT_REQ = 2;
    final static public int T6_0_CREATE_SUBJECT_REQ = 3;
    final static public int T6_0_UPDATE_SUBJECT_REQ = 4;
    final static public int T6_0_DELETE_SUBJECT_REQ = 5;
    final static public int T6_0_UPDATE_TIME_COURSE_DESCRIPTION_REQ = 6;
    final static public int T6_0_UPDATE_TIME_GRADE_ENROLLMENT_REQ = 7;
    final static public int T6_0_SELECT_PROFESSOR_REQ = 8;
    final static public int T6_0_SELECT_STUDENT_REQ = 9;
    final static public int T6_0_SELECT_ALL_OPENED_REQ = 10;
    final static public int T6_0_SELECT_GRADE_OPENED_REQ = 11;
    final static public int T6_0_SELECT_PROFESSOR_OPENED_REQ = 12;
    final static public int T6_0_SELECT_GRADE_PROFESSOR_OPENED_REQ = 13;
    final static public int T6_0_CREATE_PROFESSOR_RES = 14;
    final static public int T6_0_CREATE_STUDENT_RES = 15;
    final static public int T6_0_CREATE_SUBJECT_RES = 16;
    final static public int T6_0_UPDATE_SUBJECT_RES = 17;
    final static public int T6_0_DELETE_SUBJECT_RES = 18;
    final static public int T6_0_UPDATE_TIME_COURSE_DESCRIPTION_RES = 19;
    final static public int T6_0_UPDATE_TIME_GRADE_ENROLLMENT_RES = 20;
    final static public int T6_0_SELECT_PROFESSOR_RES = 21;
    final static public int T6_0_SELECT_STUDENT_RES = 22;
    final static public int T6_0_SELECT_ALL_OPENED_RES = 23;
    final static public int T6_0_SELECT_GRADE_OPENED_RES = 24;
    final static public int T6_0_SELECT_PROFESSOR_OPENED_RES = 25;
    final static public int T6_0_SELECT_GRADE_PROFESSOR_OPENED_RES = 26;

    //T8_0
    final static public int T8_0_BACK_TO = 0;
    final static public int T8_0_UPDATE_PRIVACY_REQ = 1;
    final static public int T8_0_INSERT_DESCRIPTION_REQ = 2;
    final static public int T8_0_UPDATE_DESCRIPTION_REQ = 3;
    final static public int T8_0_SELECT_SUBJECT_REQ = 4;
    final static public int T8_0_SELECT_DESCRIPTION_REQ = 5;
    final static public int T8_0_SELECT_OPENED_STUDENT_REQ = 6;
    final static public int T8_0_SELECT_TIMETABLE_REQ = 7;
    final static public int T8_0_INSERT_OPENED_REQ = 8;
    final static public int T8_0_UPDATE_OPENED_REQ = 9;
    final static public int T8_0_DELETE_OPENED_REQ = 10;
    final static public int T8_0_UPDATE_PRIVACY_RES = 11;
    final static public int T8_0_INSERT_DESCRIPTION_RES = 12;
    final static public int T8_0_UPDATE_DESCRIPTION_RES = 13;
    final static public int T8_0_SELECT_SUBJECT_RES = 14;
    final static public int T8_0_SELECT_DESCRIPTION_RES = 15;
    final static public int T8_0_SELECT_OPENED_STUDENT_RES = 16;
    final static public int T8_0_SELECT_TIMETABLE_RES = 17;
    final static public int T8_0_INSERT_OPENED_RES = 18;
    final static public int T8_0_UPDATE_OPENED_RES = 19;
    final static public int T8_0_DELETE_OPENED_RES = 20;

    //T9_0
    final static public int T9_0_BACK_TO = 0;
    final static public int T9_0_UPDATE_PRIVACY_REQ = 1;
    final static public int T9_0_INSERT_ENROLLMENT_REQ = 2;
    final static public int T9_0_DELETE_ENROLLMENT_REQ = 3;
    final static public int T9_0_SELECT_ALL_OPENED_REQ = 4;
    final static public int T9_0_SELECT_GRADE_OPENED_REQ = 5;
    final static public int T9_0_SELECT_PROFESSOR_OPENED_REQ = 6;
    final static public int T9_0_SELECT_GRADE_PROFESSOR_OPENED_REQ = 7;
    final static public int T9_0_SELECT_DESCRIPTION_REQ = 8;
    final static public int T9_0_SELECT_TIMETABLE_REQ = 9;
    final static public int T9_0_UPDATE_PRIVACY_RES = 10;
    final static public int T9_0_INSERT_ENROLLMENT_RES = 11;
    final static public int T9_0_DELETE_ENROLLMENT_RES = 12;
    final static public int T9_0_SELECT_ALL_OPENED_RES = 13;
    final static public int T9_0_SELECT_GRADE_OPENED_RES = 14;
    final static public int T9_0_SELECT_PROFESSOR_OPENED_RES = 15;
    final static public int T9_0_SELECT_GRADE_PROFESSOR_OPENED_RES = 16;
    final static public int T9_0_SELECT_DESCRIPTION_RES = 17;
    final static public int T9_0_SELECT_TIMETABLE_RES = 18;

    //길이
    public static final int LEN_TYPE = 1;
    public static final int LEN_CODE = 1;
    public static final int LEN_BODYLENGTH = 4;
    public static final int LEN_HEADER = 6;

    private byte type;
    private byte code;
    private byte subcode;
    private int bodyLength;
    private byte[] body;
    public Protocol() {
        this(UNDEFINED, 0);
    }
    public Protocol(int type) {
        this(type, 0);
    }

    public Protocol(int type, int code) {
        setType(type);
        setCode(code);
        setBodyLength(0);
    }

    public byte getType() {
        return type;
    }

    public void setType(int type) {
        this.type = (byte) type;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = (byte) code;
    }

    public int getBodyLength() {
        return bodyLength;
    }

    private void setBodyLength(int bodyLength) {
        this.bodyLength = bodyLength;
    }

    public Object getBody() {
        return deserialize(body);
    }

    public void setBody(Object body) {
        byte[] serializedObject = serialize(body);
        this.body = serializedObject;
        setBodyLength(serializedObject.length);
    }

    public byte[] getPacket() {
        byte[] packet = new byte[LEN_HEADER + getBodyLength()];
        packet[0] = getType();
        packet[LEN_TYPE] = getCode();
        System.arraycopy(intToByte(getBodyLength()), 0, packet, LEN_TYPE + LEN_CODE, LEN_BODYLENGTH);
        if (getBodyLength() > 0) {
            System.arraycopy(body, 0, packet, LEN_HEADER, getBodyLength());
        }
        return packet;
    }

    public void setPacketHeader(byte[] packet) {
        byte[] data;

        setType(packet[0]);
        setCode(packet[LEN_TYPE]);

        data = new byte[LEN_BODYLENGTH];
        System.arraycopy(packet, LEN_TYPE + LEN_CODE, data, 0, LEN_BODYLENGTH);
        setBodyLength(byteToInt(data));
    }

    public void setPacketBody(byte[] packet) {
        byte[] data;

        if (getBodyLength() > 0) {
            data = new byte[getBodyLength()];
            System.arraycopy(packet, 0, data, 0, getBodyLength());
            setBody(deserialize(data));
        }
    }

    private byte[] serialize(Object b) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(b);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Object deserialize(byte[] b) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object ob = ois.readObject();
            return ob;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private byte[] intToByte(int i) {
        return ByteBuffer.allocate(Integer.SIZE / 8).putInt(i).array();
    }

    private int byteToInt(byte[] b) {
        return ByteBuffer.wrap(b).getInt();
    }
}
