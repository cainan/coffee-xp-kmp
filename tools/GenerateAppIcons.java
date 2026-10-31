import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.imageio.ImageIO;

/** Single source for the Coffee Xp brand mark and every Android/Desktop icon. */
public final class GenerateAppIcons {
    static final Color ESPRESSO = new Color(0x271310);
    static final Color PAPER = new Color(0xFAF9F5);
    static final Color ROSE = new Color(0xE3BEB8);
    // Balance the dominant filter/cup axis, rather than the extended handle.
    static final double MARK_CENTER_X = 47.5;
    static final double MARK_CENTER_Y = 52;
    record Ink(Shape shape, Color color) {}
    static Path2D path(double... v) {
        Path2D p = new Path2D.Double(); p.moveTo(v[0], v[1]);
        for (int i=2; i<v.length; i+=6) p.curveTo(v[i],v[i+1],v[i+2],v[i+3],v[i+4],v[i+5]);
        return p;
    }
    static List<Ink> mark(boolean tiny) {
        List<Ink> a = new ArrayList<>();
        // Recognisable cup handle sits behind the cup, with generous open counter.
        a.add(new Ink(new BasicStroke(5.5f,1,1).createStrokedShape(
            path(62,60, 73,57,73,72,62,70)), PAPER));
        Path2D cup = path(29,57, 40,57,53,57,64,57, 64,60,64,63,64,65,
            64,75,57,80,47,80, 37,80,29,75,29,65, 29,62,29,59,29,57);
        cup.closePath(); a.add(new Ink(cup,PAPER));
        Path2D inside = path(34,62, 42,62,51,62,59,62, 59,63,59,64,59,65,
            59,71,54,75,47,75, 40,75,34,71,34,65, 34,64,34,63,34,62);
        inside.closePath(); a.add(new Ink(inside,ESPRESSO));
        if (!tiny) {
            Path2D bookmark = new Path2D.Double();
            bookmark.moveTo(43.5,62);bookmark.lineTo(50.5,62);bookmark.lineTo(50.5,71.5);
            bookmark.lineTo(47,69);bookmark.lineTo(43.5,71.5);bookmark.closePath();
            a.add(new Ink(bookmark,ROSE));
        } else {
            a.add(new Ink(new RoundRectangle2D.Double(38,67,18,4,3,3),ROSE));
        }
        // Short ceramic cone; grooves end before the outlet instead of colliding.
        Path2D cone = path(23,29, 33,31,61,31,71,29,
            67,38,58,48,51,53, 49,55,45,55,43,53, 36,48,27,38,23,29);
        cone.closePath(); a.add(new Ink(cone,PAPER));
        float ribWidth = tiny ? 3.2f : 2.7f;
        a.add(new Ink(new BasicStroke(ribWidth,1,1).createStrokedShape(
            path(36,35, 38,40,41,45,43,48)), ESPRESSO));
        a.add(new Ink(new BasicStroke(ribWidth,1,1).createStrokedShape(
            path(58,35, 56,40,53,45,51,48)), ESPRESSO));
        a.add(new Ink(new Ellipse2D.Double(22,24,50,9),ROSE));
        a.add(new Ink(new Ellipse2D.Double(27,26.5,40,4),ESPRESSO));
        return a;
    }
    static AffineTransform placement(double scale, double center) {
        AffineTransform t = AffineTransform.getTranslateInstance(center,center);
        t.scale(scale,scale); t.translate(-MARK_CENTER_X,-MARK_CENTER_Y); return t;
    }
    static Shape placed(Shape s,double scale,double center) { return placement(scale,center).createTransformedShape(s); }
    static void draw(Graphics2D g, int size, boolean tiny) {
        g.scale(size/100.0,size/100.0);
        for (Ink i:mark(tiny)) {g.setColor(i.color());g.fill(placed(i.shape(),1.16,50));}
    }
    static BufferedImage render(int size, boolean round) {
        int ss=4;
        BufferedImage hi=new BufferedImage(size*ss,size*ss,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g=hi.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(ESPRESSO);
        if(round)g.fillOval(0,0,hi.getWidth(),hi.getHeight());
        else g.fill(new RoundRectangle2D.Double(0,0,hi.getWidth(),hi.getHeight(),hi.getWidth()*.25,hi.getHeight()*.25));
        draw(g,hi.getWidth(),size<=24);g.dispose();
        BufferedImage out=new BufferedImage(size,size,BufferedImage.TYPE_INT_ARGB);
        g=out.createGraphics();g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(hi,0,0,size,size,null);g.dispose();return out;
    }
    static String data(Shape s) {
        StringBuilder b=new StringBuilder();double[] v=new double[6];
        for(PathIterator p=s.getPathIterator(null);!p.isDone();p.next()) {
            int type=p.currentSegment(v);int n=switch(type){case 0,1->2;case 2->4;case 3->6;default->0;};
            b.append(switch(type){case 0->"M";case 1->"L";case 2->"Q";case 3->"C";default->"Z";});
            for(int j=0;j<n;j++){if(j>0)b.append(',');b.append(String.format(Locale.ROOT,"%.4f",v[j]));}
        }return b.toString();
    }
    static String hex(Color c){return String.format("#%06X",c.getRGB()&0xffffff);}
    static void text(Path p,String s)throws Exception{Files.createDirectories(p.getParent());Files.writeString(p,s);}
    static void vectors(Path root)throws Exception {
        StringBuilder svg=new StringBuilder("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 100 100\">\n<rect width=\"100\" height=\"100\" rx=\"12.5\" fill=\"#271310\"/>\n");
        StringBuilder transparent=new StringBuilder("<svg xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 100 100\">\n");
        StringBuilder xml=new StringBuilder("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<vector xmlns:android=\"http://schemas.android.com/apk/res/android\" android:width=\"108dp\" android:height=\"108dp\" android:viewportWidth=\"108\" android:viewportHeight=\"108\">\n");
        Area mono=new Area(), paperArea=new Area(), roseArea=new Area();
        for(Ink i:mark(false)) {
            String s="<path fill=\""+hex(i.color())+"\" d=\""+data(placed(i.shape(),1.16,50))+"\"/>\n";
            svg.append(s);
            Area visible = new Area(placed(i.shape(),1.16,50));
            if(i.color().equals(ESPRESSO)){paperArea.subtract(visible);roseArea.subtract(visible);}
            else if(i.color().equals(PAPER)){roseArea.subtract(visible);paperArea.add(visible);}
            else {paperArea.subtract(visible);roseArea.add(visible);}
            Shape android=placed(i.shape(),.84,54);
            xml.append("<path android:fillColor=\"").append(hex(i.color())).append("\" android:pathData=\"").append(data(android)).append("\"/>\n");
            if(i.color().equals(ESPRESSO))mono.subtract(new Area(android));else mono.add(new Area(android));
        }
        text(root.resolve("brand/coffee-xp-icon.svg"),svg+"</svg>\n");
        transparent.append("<path fill=\"#FAF9F5\" d=\"").append(data(paperArea)).append("\"/>\n");
        transparent.append("<path fill=\"#E3BEB8\" d=\"").append(data(roseArea)).append("\"/>\n");
        text(root.resolve("brand/coffee-xp-mark.svg"),transparent+"</svg>\n");
        text(root.resolve("androidApp/src/main/res/drawable-v24/ic_launcher_foreground.xml"),xml+"</vector>\n");
        String m="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<vector xmlns:android=\"http://schemas.android.com/apk/res/android\" android:width=\"108dp\" android:height=\"108dp\" android:viewportWidth=\"108\" android:viewportHeight=\"108\"><path android:fillColor=\"#FFFFFFFF\" android:pathData=\""+data(mono)+"\"/></vector>\n";
        text(root.resolve("androidApp/src/main/res/drawable-v24/ic_launcher_monochrome.xml"),m);
        String adaptive="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n<adaptive-icon xmlns:android=\"http://schemas.android.com/apk/res/android\"><background android:drawable=\"@drawable/ic_launcher_background\"/><foreground android:drawable=\"@drawable/ic_launcher_foreground\"/><monochrome android:drawable=\"@drawable/ic_launcher_monochrome\"/></adaptive-icon>\n";
        for(String name:List.of("ic_launcher","ic_launcher_round"))text(root.resolve("androidApp/src/main/res/mipmap-anydpi-v33/"+name+".xml"),adaptive);
    }
    static void preview(Path root)throws Exception {
        BufferedImage sheet=new BufferedImage(1050,560,BufferedImage.TYPE_INT_ARGB);Graphics2D g=sheet.createGraphics();
        g.setColor(new Color(0xE8E8E4));g.fillRect(0,0,1050,560);g.setColor(ESPRESSO);g.setFont(new Font("SansSerif",Font.BOLD,22));
        g.drawString("Coffee Xp | Android + Desktop",30,38);
        int[] sizes={16,24,32,48,64,128};int x=30;
        for(int n:sizes){g.setColor(ESPRESSO);g.setFont(new Font("SansSerif",0,15));g.drawString(n+" px",x,80);g.drawImage(render(n,false),x,100,null);g.drawImage(render(n,true),x,250,null);x+=n+38;}
        g.drawImage(render(256,false),770,90,null);
        // Android adaptive drawable: foreground is rendered on 108dp, then the
        // launcher exposes the central 72dp. Test several masks and full motion.
        int px=30;
        for(int variant=0;variant<4;variant++) {
            int n=120;BufferedImage tile=new BufferedImage(n,n,2);Graphics2D t=tile.createGraphics();
            t.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
            Shape mask=variant==0?new Ellipse2D.Double(0,0,n,n):variant==1?new RoundRectangle2D.Double(0,0,n,n,45,45):new Rectangle(0,0,n,n);
            t.setClip(mask);t.setColor(variant==3?new Color(0xE3BEB8):ESPRESSO);t.fillRect(0,0,n,n);
            t.scale(n/72.0,n/72.0);t.translate(-18,-18);
            Area silhouette=new Area();
            for(Ink i:mark(false)){
                Shape shape=placed(i.shape(),.84,54);
                if(variant==3){if(i.color().equals(ESPRESSO))silhouette.subtract(new Area(shape));else silhouette.add(new Area(shape));}
                else {t.setColor(i.color());t.fill(shape);}
            }
            if(variant==3){t.setColor(ESPRESSO);t.fill(silhouette);}t.dispose();
            g.drawImage(tile,px,405,null);g.setColor(ESPRESSO);g.drawString(new String[]{"Adaptive circle","Squircle","Square","Themed icon"}[variant],px,550);px+=180;
        }
        g.dispose();writePng(sheet,root.resolve("brand/icon-preview.png"));
        // All coloured foreground pixels must survive the Android guaranteed
        // 66dp-diameter safe circle (including the complete cup handle).
        Area outside=new Area();for(Ink i:mark(false))if(!i.color().equals(ESPRESSO))outside.add(new Area(placed(i.shape(),.84,54)));
        outside.subtract(new Area(new Ellipse2D.Double(21,21,66,66)));
        if(!outside.isEmpty())throw new IllegalStateException("Foreground exceeds adaptive icon safe circle");
        Area bounds=new Area();
        for(Ink i:mark(false))if(!i.color().equals(ESPRESSO))bounds.add(new Area(placed(i.shape(),1.16,50)));
        Rectangle2D box=bounds.getBounds2D();
        if(Math.abs(box.getCenterX()-50)>.3 || Math.abs(box.getCenterY()-50)>.3)
            throw new IllegalStateException("Mark bounds are no longer centered");
        Point2D axis=placement(1.16,50).transform(new Point2D.Double(47,52),null);
        if(Math.abs(axis.getX()-50)>.6)throw new IllegalStateException("Filter/cup axis is not optically centered");
        System.out.println("PASS: balanced bounds and centered filter/cup axis");
        System.out.println("PASS: adaptive safe circle, supersampled raster exports, small icon variants");
    }
    private static void writePng(BufferedImage image, Path path) throws Exception {
        Files.createDirectories(path.getParent());
        ImageIO.write(image, "png", path.toFile());
    }

    private static void writeShortLe(DataOutputStream out, int value) throws Exception {
        out.writeByte(value & 0xff);
        out.writeByte((value >>> 8) & 0xff);
    }

    private static void writeIntLe(DataOutputStream out, int value) throws Exception {
        out.writeByte(value & 0xff);
        out.writeByte((value >>> 8) & 0xff);
        out.writeByte((value >>> 16) & 0xff);
        out.writeByte((value >>> 24) & 0xff);
    }

    private static void writeIco(Path path) throws Exception {
        int[] sizes = {16, 20, 24, 32, 40, 48, 64, 128, 256};
        List<byte[]> pngs = new ArrayList<>();
        for (int size : sizes) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            ImageIO.write(render(size, false), "png", bytes);
            pngs.add(bytes.toByteArray());
        }
        Files.createDirectories(path.getParent());
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(path.toFile()))) {
            writeShortLe(out, 0);
            writeShortLe(out, 1);
            writeShortLe(out, sizes.length);
            int offset = 6 + 16 * sizes.length;
            for (int i = 0; i < sizes.length; i++) {
                int size = sizes[i];
                byte[] png = pngs.get(i);
                out.writeByte(size == 256 ? 0 : size);
                out.writeByte(size == 256 ? 0 : size);
                out.writeByte(0);
                out.writeByte(0);
                writeShortLe(out, 1);
                writeShortLe(out, 32);
                writeIntLe(out, png.length);
                writeIntLe(out, offset);
                offset += png.length;
            }
            for (byte[] png : pngs) out.write(png);
        }
    }

    private static void writeIcns(Path path) throws Exception {
        int[] sizes = {16, 32, 64, 128, 256, 512, 1024, 32, 64, 256, 512};
        String[] types = {"icp4", "icp5", "icp6", "ic07", "ic08", "ic09", "ic10", "ic11", "ic12", "ic13", "ic14"};
        List<byte[]> chunks = new ArrayList<>();
        int totalLength = 8;
        for (int i = 0; i < sizes.length; i++) {
            ByteArrayOutputStream pngBytes = new ByteArrayOutputStream();
            ImageIO.write(render(sizes[i], false), "png", pngBytes);
            byte[] png = pngBytes.toByteArray();
            ByteArrayOutputStream chunk = new ByteArrayOutputStream();
            DataOutputStream data = new DataOutputStream(chunk);
            data.writeBytes(types[i]);
            data.writeInt(8 + png.length);
            data.write(png);
            chunks.add(chunk.toByteArray());
            totalLength += 8 + png.length;
        }
        Files.createDirectories(path.getParent());
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(path.toFile()))) {
            out.writeBytes("icns");
            out.writeInt(totalLength);
            for (byte[] chunk : chunks) out.write(chunk);
        }
    }

    public static void main(String[] args) throws Exception {
        Path root = Path.of(args.length == 0 ? "." : args[0]).toAbsolutePath();
        Path android = root.resolve("androidApp/src/main/res");
        String[] densities = {"mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi"};
        int[] sizes = {48, 72, 96, 144, 192};
        for (int i = 0; i < sizes.length; i++) {
            writePng(render(sizes[i], false), android.resolve("mipmap-" + densities[i] + "/ic_launcher.png"));
            writePng(render(sizes[i], true), android.resolve("mipmap-" + densities[i] + "/ic_launcher_round.png"));
        }
        Path desktop = root.resolve("desktopApp/src/main/resources");
        writePng(render(1024, false), desktop.resolve("coffee-xp-icon.png"));
        for (int size : new int[]{16,20,24,32,40,48,64,128,256})
            writePng(render(size, false), desktop.resolve("icons/coffee-xp-" + size + ".png"));
        writeIco(desktop.resolve("coffee-xp-icon.ico"));
        writeIcns(desktop.resolve("coffee-xp-icon.icns"));
        vectors(root);
        preview(root);
        verifyExports(root);
    }

    private static void verifyExports(Path root) throws Exception {
        Path desktop=root.resolve("desktopApp/src/main/resources");
        for(int size:new int[]{16,20,24,32,40,48,64,128,256}) {
            BufferedImage image=ImageIO.read(desktop.resolve("icons/coffee-xp-"+size+".png").toFile());
            if(image==null || image.getWidth()!=size || image.getHeight()!=size)
                throw new IllegalStateException("Invalid Desktop window icon: "+size);
        }
        byte[] bytes=Files.readAllBytes(desktop.resolve("coffee-xp-icon.ico"));
        java.nio.ByteBuffer b=java.nio.ByteBuffer.wrap(bytes).order(java.nio.ByteOrder.LITTLE_ENDIAN);
        if(b.getShort()!=0 || b.getShort()!=1)throw new IllegalStateException("Invalid ICO header");
        int count=Short.toUnsignedInt(b.getShort());
        for(int i=0;i<count;i++){
            int width=Byte.toUnsignedInt(b.get()), height=Byte.toUnsignedInt(b.get());
            if(width==0)width=256;if(height==0)height=256;
            b.position(b.position()+6);int length=b.getInt(), offset=b.getInt();
            BufferedImage image=ImageIO.read(new ByteArrayInputStream(bytes,offset,length));
            if(image.getWidth()!=width || image.getHeight()!=height)throw new IllegalStateException("ICO size mismatch");
        }
        bytes=Files.readAllBytes(desktop.resolve("coffee-xp-icon.icns"));
        b=java.nio.ByteBuffer.wrap(bytes);
        if(b.getInt()!=0x69636e73 || b.getInt()!=bytes.length)throw new IllegalStateException("Invalid ICNS header");
        while(b.hasRemaining()){
            b.getInt();int length=b.getInt();int start=b.position();
            if(length<8 || start+length-8>bytes.length)throw new IllegalStateException("Invalid ICNS chunk");
            if(ImageIO.read(new ByteArrayInputStream(bytes,start,length-8))==null)throw new IllegalStateException("Invalid ICNS raster");
            b.position(start+length-8);
        }
        for(String density:new String[]{"mdpi","hdpi","xhdpi","xxhdpi","xxxhdpi"}){
            Path p=root.resolve("androidApp/src/main/res/mipmap-"+density);
            BufferedImage round=ImageIO.read(p.resolve("ic_launcher_round.png").toFile());
            if((round.getRGB(0,0)>>>24)!=0)throw new IllegalStateException("Round icon corner must be transparent");
        }
        System.out.println("PASS: all 9 ICO entries and 11 ICNS representations decode; Android PNG transparency");
    }
}

