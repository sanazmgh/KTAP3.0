package shared.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

public class ShareImage
{
    public static byte[] decodeFromBase64(String str)
    {
        if (str == null)
            return null;
        return Base64.getDecoder().decode(str);
    }

    public static String encodeToBase64(byte[] bytes)
    {
        if (bytes == null)
            return null;
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String imageToString(String path)
    {
        String picture = "";
        if (!path.equals(""))
        {
            File image = new File(path);
            if(image.exists())
            {
                try
                {
                    picture = ShareImage.encodeToBase64(Files.readAllBytes(image.toPath()));
                } catch (IOException ignored) {}
            }
        }
        return picture;
    }
}
