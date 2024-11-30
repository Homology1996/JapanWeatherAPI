package main.controller;

import main.Constants;
import main.MainFrame;
import main.service.APIService;
import main.service.ExcelService;
import main.view.PanelUtility;
import main.view.panel.ResultPanel;
import net.lingala.zip4j.ZipFile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GenerateReportController extends SwingWorker<String, Double> {

    private final static Logger LOGGER = LogManager.getLogger(GenerateReportController.class);

    private final ResultPanel resultPanel;

    private String status;

    public GenerateReportController(ResultPanel resultPanel) {
        this.resultPanel = resultPanel;
    }

    @Override
    protected String doInBackground() {
        JButton nextStep = (JButton) MainFrame.getComponentMapForPanel(this.resultPanel)
                .get(PanelUtility.getComponentName(Constants.RESULT_PANEL, Constants.RESULT_NEXT));
        nextStep.setEnabled(false);
        JFileChooser downloadChooser = new JFileChooser();
        downloadChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        downloadChooser.setName(PanelUtility.getComponentName(Constants.RESULT_PANEL, Constants.RESULT_DOWNLOAD_CHOOSER));
        this.resultPanel.add(downloadChooser);
        switch (downloadChooser.showOpenDialog(null)) {
            case JFileChooser.APPROVE_OPTION:
                MainFrame.setStatusAndProgressBar(Constants.EMPTY, 0);
                MainFrame.setStatusAndProgressBarVisibility(true);
                File selectedFile = downloadChooser.getSelectedFile();
                if (selectedFile != null && selectedFile.isDirectory()) {
                    this.status = Constants.MSG_CREATE_ZIP;
                    this.publish(30.0);
                    String fileName = Constants.ZIP_FILE_NAME;
                    File zipInFile = new File(selectedFile.toPath() + File.separator + fileName);
                    this.createReadableAndWritableZip(zipInFile);
                    if (zipInFile.isFile() && zipInFile.length() > 0) {
                        int number = 1;
                        while (true) {
                            String alternativeFileName = String.format(Constants.ZIP_FILE_NAME_FORMAT, number);
                            File alternativeZipInFile = new File(selectedFile.toPath() + File.separator + alternativeFileName);
                            if (alternativeZipInFile.isFile() && alternativeZipInFile.length() > 0) {
                                number++;
                            } else {
                                fileName = alternativeFileName;
                                zipInFile = alternativeZipInFile;
                                this.createReadableAndWritableZip(zipInFile);
                                break;
                            }
                        }
                    }
                    this.status = Constants.MSG_HANDLE_ZIP;
                    this.publish(60.0);
                    ZipFile zipFile = new ZipFile(zipInFile);
                    XSSFWorkbook workbook = ExcelService.workbook;
                    if (workbook != null) {
                        ExcelService.addWorkbookToZipFile(zipFile, workbook, Constants.EXCEL_FILE_NAME);
                        try {
                            workbook.close();
                        } catch (IOException ioe) {
                            LOGGER.error("Unable to close workbook", ioe);
                        }
                        return Constants.SUCCEED + fileName + Constants.MSG_DOWNLOAD_SUCCEED;
                    } else {
                        LOGGER.error("workbook is null");
                        return Constants.FAIL + Constants.MSG_REPORT_IS_NULL;
                    }
                } else {
                    if (selectedFile == null) {
                        LOGGER.error("Unable to load directory since it's null");
                    } else {
                        LOGGER.error("Unable to load directory with absolute path = " + selectedFile.getAbsolutePath());
                    }
                    return Constants.FAIL + Constants.MSG_LOAD_FOLDER_FAIL;
                }
            case JFileChooser.CANCEL_OPTION:
                LOGGER.info(downloadChooser.getName() + " is cancelled or closed");
                return Constants.FAIL + Constants.MSG_DOWNLOAD_FILE_CANCEL;
            default:
                LOGGER.error("Unable to download file");
                return Constants.FAIL + Constants.MSG_DOWNLOAD_FILE_FAIL;
        }
    }

    @Override
    protected void process(List<Double> chunks) {
        MainFrame.setStatusAndProgressBar(this.status, Integer.parseInt(Long.toString(Math.round(chunks.get(chunks.size() - 1)))));
    }

    @Override
    protected void done() {
        APIService.startDate = Constants.EMPTY;
        APIService.endDate = Constants.EMPTY;
        MainFrame.setStatusAndProgressBarVisibility(false);
    }

    /**
     * 建立一個可讀可寫的壓縮檔
     * */
    private void createReadableAndWritableZip(File file) {
        if (!file.exists() || file.length() <= 1) {
            try {
                String absolutePath = file.getAbsolutePath();
                String parentFolderPath = absolutePath.substring(0,
                        absolutePath.length() - file.getName().length() - File.separator.length());
                File parentFolder = new File(parentFolderPath);
                if (!parentFolder.exists() && parentFolder.mkdirs()) {
                    LOGGER.info(parentFolderPath + " is created");
                }
                if (file.createNewFile()) {
                    LOGGER.info(file.getName() + " is created");
                }
            } catch (IOException ioe) {
                LOGGER.error("Unable to create " + file.getName(), ioe);
            }
        }
        if (!file.canRead()) {
            if (file.setReadable(true, false)) {
                LOGGER.info(file.getName() + " is set readable");
            } else {
                LOGGER.error("Unable to set " + file.getName() + " readable");
            }
        }
        if (!file.canWrite()) {
            if (file.setWritable(true, false)) {
                LOGGER.info(file.getName() + " is set writable");
            } else {
                LOGGER.error("Unable to set " + file.getName() + " writable");
            }
        }
    }

}