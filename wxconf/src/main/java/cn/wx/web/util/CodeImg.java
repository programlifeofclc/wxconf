package cn.wx.web.util;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.HashMap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class CodeImg {

	public static void getImg(String content, int width, int height, String format, File file) {
		// 定义二维码的参数
		HashMap<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 2);
		// 生成二维码
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			Path path = file.toPath();
			MatrixToImageWriter.writeToPath(bitMatrix, format, path);
		} catch (Exception e) {

		}
	}

	public static void getImgToStream(String content, int width, int height, String format, OutputStream out) {
		// 定义二维码的参数
		HashMap<EncodeHintType, Object> hints = new HashMap<>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
		hints.put(EncodeHintType.MARGIN, 2);
		// 生成二维码
		try {
			// OutputStream stream = new OutputStreamWriter();
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
			MatrixToImageWriter.writeToStream(bitMatrix, format, out);
		} catch (Exception e) {

		}
	}
	
	
//	public static void main(String[] args) throws Exception {
//		20         MultiFormatReader formatReader = new MultiFormatReader();
//		21         File file = new File("F:/img.png");
//		22         
//		23         BufferedImage image = ImageIO.read(file);
//		24         BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
//		25         
//		26         //定义二维码的参数
//		27         HashMap hints = new HashMap();
//		28         hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
//		29         
//		30         Result result = formatReader.decode(binaryBitmap, hints); 
//		31 
//		32         System.out.println("二维码解析结果：" + result.toString());
//		33         System.out.println("二维码的格式：" + result.getBarcodeFormat());
//		34         System.out.println("二维码的文本内容：" + result.getText());
//		35     }

}
