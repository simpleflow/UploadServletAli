import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lotus.domino.*;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/**
 *
 * Servlet implementation class testServlet
 */
@WebServlet("/UploadServletAli")
public class UploadServletAli extends HttpServlet {
    private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UploadServletAli() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see Servlet#init(ServletConfig)
     */
    public void init(ServletConfig config) throws ServletException {
        // TODO Auto-generated method stub
        //http://127.0.0.1:8082/myServelt/hello
        System.out.print("init");

    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        System.out.print("doGet");
    }

    private Session getSession() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        // 上传文件目录
        //String uploadDir = this.getServletContext().getRealPath("/upload");
        String uploadDir = "";
        PrintWriter out = response.getWriter();
        // out.println("Hello, upload 111!");
        String serverDir = null;
        System.out.print("start doPost");
        /*---------------------*/
        // try {
        NotesThread.sinitThread();
        serverDir = "D:\\Program Files\\IBM\\Domino\\data";
        DiskFileItemFactory factory = new DiskFileItemFactory();
        // 设置内存区块大小4KB
        factory.setSizeThreshold(4 * 1024);
        // 设置暂存容器，当上传文件大于设置的内存块大小时，用暂存容器做中转
        //factory.setRepository(new File(this.getServletContext().getRealPath("e:/temp")));
        ServletFileUpload fileUpload = new ServletFileUpload(factory);
        fileUpload.setSizeMax(1024 * 1024 * 1000);
        //fileUpload.setFileSizeMax(1024 * 1024 * 10);
        fileUpload.setHeaderEncoding("UTF-8");// 解决文件名乱码的问题
        List<FileItem> fileItemList = null;
        try {
            fileItemList = fileUpload.parseRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator<FileItem> fileItemIterator = fileItemList.iterator();
        FileItem fileItem = null;
        while (fileItemIterator.hasNext()) {
            fileItem = fileItemIterator.next();
            // 普通文件框上传
            if (fileItem.isFormField()) {
                String filedName = fileItem.getFieldName();
                String filedValue = fileItem.getString("UTF-8");// 编码格式
                System.out.println(filedName+"__"+filedValue);// 文件框名称
                //System.out.println(filedValue);// 文件的值
                //获取文件夹目录
                if (filedName.equals("LOADFOLDER")){
                    uploadDir = serverDir+"\\domino\\html\\"+filedValue;
                    System.out.print(uploadDir);
                }else{
                    //System.out.print("找不到系统目录");
                }
            } else {
                String filedName = fileItem.getFieldName();// 文件上传框的名称
                // 获取文件上传的文件名
                String OriginalFileName = takeOutFileName(fileItem.getName());
                System.out.println("原始文件名："+OriginalFileName);
                File writeToFile = new File(uploadDir + File.separator
                        + OriginalFileName);
                try {
                    fileItem.write(writeToFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                //以下为文件重命名
                /*
                if (!"".equals(OriginalFileName)) {
                    // 根据上传的文件名重新命名
                    String newFileName = getNewFileName(OriginalFileName);
                    //System.out.println("重新名："+newFileName);
                    File writeToFile = new File(uploadDir + File.separator
                            + newFileName);
                    try {
                        fileItem.write(writeToFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                */
            }
        }
        System.out.print("end post");


    }

    /**
     * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
     */
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }

    /**
     * //@see HttpServlet#doHead(HttpServletRequest, HttpServletResponse)
     */
    protected void doHead(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
    }
    private String takeOutFileName(String filePath) {
        String fileName = filePath;
        if (null != filePath && !"".equals(filePath)) {
            int port = filePath.lastIndexOf("\\");
            if(port != -1){
                fileName = filePath.substring(port+1);
            }
        }
        return fileName;
    }

    private String getNewFileName(String originalFileName) {
        StringBuffer newFileName = new StringBuffer();
        if (null != originalFileName && !"".equals(originalFileName)) {
            int port = originalFileName.lastIndexOf(".");
            String type = "";
            String fileName = "";
            if (port != -1) {
                type = originalFileName.substring(port + 1);
                fileName = originalFileName.substring(0, port);
            } else {
                fileName = originalFileName;
            }
            StringBuffer suffix = new StringBuffer("_");
            suffix.append(Calendar.getInstance().getTimeInMillis());
            suffix.append("_");
            suffix.append(new Random().nextInt(100));
            newFileName.append(fileName);
            newFileName.append(suffix);
            newFileName.append(".");
            newFileName.append(type);
        }
        return newFileName.toString();
    }

}
