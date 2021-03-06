package top.iseason.BookMail.Util;

import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTContainer;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ItemTranslator {
    public static String itemToZipString(ItemStack item) {
        NBTCompound itemData = NBTItem.convertItemtoNBT(item);
        return zipString(itemData.toString());
    }

    public static String itemToNBTString(ItemStack item) {
        NBTCompound itemData = NBTItem.convertItemtoNBT(item);
        return itemData.toString();
    }

    public static String itemListToZipString(List<ItemStack> itemList) {
        StringBuilder data = new StringBuilder();
        for (ItemStack item : itemList) {
            if (item != null) {
                data.append("<|>"); //分隔符
                NBTCompound itemData = NBTItem.convertItemtoNBT(item);
                String nbtData = itemData.toString();
                data.append(nbtData).append("<|>"); //分隔符
            }
        }
        return zipString(data.toString());
    }

    public static List<ItemStack> zipStringToItemList(String zipString) {
        String nbtString = unzipString(zipString);
        List<ItemStack> itemList = new ArrayList<>();
        if (nbtString != null) {
            Matcher matcher = Pattern.compile("<\\|>([\\s\\S]*?)<\\|>").matcher(nbtString);
            while (matcher.find()) {
                itemList.add(nbtStringToItem(matcher.group(1)));
            }
        }
        return itemList;
    }

    public static ItemStack nbtStringToItem(String nbtString) {
        NBTContainer cont = new NBTContainer(nbtString);
        return NBTItem.convertNBTtoItem(cont);
    }

    public static ItemStack zipStringToItem(String zipString) {
        String data = unzipString(zipString);
        return nbtStringToItem(data);
    }
    public static String zipString(String unzipString) {
        Deflater deflater = new Deflater(Deflater.BEST_SPEED);
        deflater.setInput(unzipString.getBytes(StandardCharsets.UTF_8));
        deflater.finish();
        final byte[] bytes = new byte[512];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        while (!deflater.finished()) {
            int length = deflater.deflate(bytes);
            outputStream.write(bytes, 0, length);
        }
        deflater.end();
        return Base64.getEncoder().encodeToString(outputStream.toByteArray());
    }

    public static String unzipString(String zipString) {
        byte[] decode = Base64.getDecoder().decode(zipString);
        Inflater inflater = new Inflater();
        inflater.setInput(decode);
        final byte[] bytes = new byte[512];
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(512);
        try {
            while (!inflater.finished()) {
                int length = inflater.inflate(bytes);
                outputStream.write(bytes, 0, length);
            }
        } catch (DataFormatException e) {
            e.printStackTrace();
            return null;
        } finally {
            inflater.end();
        }
        try {
            return outputStream.toString("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }


}
