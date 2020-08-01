package ace.fw.logic.common.util;

import com.fasterxml.uuid.Generators;

import java.util.Base64;
import java.util.UUID;

/**
 * @author Caspar
 * @contract 279397942@qq.com
 * @create 2020/7/31 9:57
 * @description uuid 的工具类
 */
public class AceUUIDUtils {

    /**
     * v1 version time base uuid,去掉 "-"
     *
     * @return
     */
    public static String generateTimeUUIDShort32() {
        return generateTimeUUIDString().replace("-", "");
    }

    /**
     * V1 version time base uuid
     *
     * @return uuid string
     */
    public static String generateTimeUUIDString() {
        return generateTimeUUID().toString();
    }

    /**
     * V1 version time base uuid
     *
     * @return uuid
     */
    public static UUID generateTimeUUID() {
        return Generators.timeBasedGenerator().generate();
    }

    /**
     * generate time base uuid v1,base64 22byte
     *
     * @return
     */
    public static String generateTimeUUIDCompress() {
        return generateUUIDCompress(generateTimeUUID());
    }

    /**
     * 压缩
     *
     * @param uuid uuid字符串，可带有{@code -}
     * @return base64字符串，length=22
     */
    public static String generateUUIDCompress(UUID uuid) {
        long msb = uuid.getMostSignificantBits();
        long lsb = uuid.getLeastSignificantBits();

        byte[] b = new byte[16];
        for (int i = 0; i < 8; i++) {
            b[i] = (byte) (msb >>> (8 * (7 - i)) & 0xff);
            b[i + 8] = (byte) (lsb >>> (8 * (7 - i)) & 0xff);
        }

        return Base64.getEncoder().withoutPadding().encodeToString(b);
    }

    /**
     * 解压
     *
     * @param srcCompress base64字符串，length=22
     * @return uuid字符串，{@code -}分割
     */
    public static UUID generateUUIDFromDecompress(String srcCompress) {
        byte[] b = Base64.getDecoder().decode(srcCompress);

        long msb = 0;
        long lsb = 0;
        for (int i = 0; i < 8; i++) {
            msb = (msb << 8) | (b[i] & 0xff);
            lsb = (lsb << 8) | (b[i + 8] & 0xff);
        }

        return new UUID(msb, lsb);
    }

    public static void main(String[] args) {
        UUID timeUUID = generateTimeUUID();

        System.out.println("uuid:" + timeUUID.toString());

        System.out.println("clockSequence:" + timeUUID.clockSequence());

        System.out.println("version:" + timeUUID.version());

        System.out.println("variant:" + timeUUID.variant());

        System.out.println("timestamp:" + timeUUID.timestamp());

        String uuidCompress = generateUUIDCompress(timeUUID);
        System.out.println("generateUUIDCompress:" + uuidCompress);

        System.out.println("generateUUIDFromDecompress:" + generateUUIDFromDecompress(uuidCompress));
    }
}
