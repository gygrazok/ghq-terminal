package com.terredegliangeli.ghq.terminal.service

import com.terredegliangeli.ghq.terminal.configuration.ApplicationProperties
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.printing.PDFPageable
import org.springframework.stereotype.Service
import java.awt.print.PrinterJob
import java.io.File
import javax.print.PrintService
import javax.print.PrintServiceLookup


@Service
class FilePrintService(private val applicationProperties: ApplicationProperties) {
    fun printFile(file: String) {
        val pdfFile = {}::class.java.classLoader.getResourceAsStream(file)

        val myPdfFile = this::class.java.classLoader.getResource(file)?.toURI()?.let { File(it) }
        if (myPdfFile == null || !myPdfFile.exists()) {
            return
        }

        val printService = findPrintService(applicationProperties.printerName)
            ?: throw IllegalArgumentException("Printer not found: ${applicationProperties.printerName}")
        val myDoc: PDDocument = PDDocument.load(myPdfFile)
        val printJob = PrinterJob.getPrinterJob()
        printJob.printService = printService
        printJob.setPageable(PDFPageable(myDoc))
        printJob.print()
        pdfFile?.close()
    }

    private fun findPrintService(printerName: String): PrintService? {
        val services = PrintServiceLookup.lookupPrintServices(null, null)
        for (service in services) {
            if (service.name.equals(printerName, ignoreCase = true)) {
                return service
            }
        }
        return null
    }
}