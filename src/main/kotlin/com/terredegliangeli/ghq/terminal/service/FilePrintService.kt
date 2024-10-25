package com.terredegliangeli.ghq.terminal.service

import com.terredegliangeli.ghq.terminal.configuration.ApplicationProperties
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.printing.PDFPageable
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.awt.print.PrinterJob
import java.io.File
import javax.print.PrintService
import javax.print.PrintServiceLookup


@Service
class FilePrintService(private val applicationProperties: ApplicationProperties) {
    val logger = LoggerFactory.getLogger(FilePrintService::class.java)
    fun printFile(file: String) {
        logger.info("Printing file: $file")
        val pdfFile = {}::class.java.classLoader.getResourceAsStream(file)

        requireNotNull(pdfFile) { "File does not exist: $file" }
        val myPdfFile = this::class.java.classLoader.getResource(file)?.toURI()?.let { File(it) }
        if (myPdfFile == null || !myPdfFile.exists()) {
            println("File does not exist")
            return
        }

        val printService = findPrintService(applicationProperties.printerName)
            ?: throw IllegalArgumentException("Printer not found: ${applicationProperties.printerName}")
        val myDoc: PDDocument = PDDocument.load(myPdfFile)
        val printJob = PrinterJob.getPrinterJob()
        printJob.printService = printService
        printJob.setPageable(PDFPageable(myDoc))
        printJob.print()

        logger.info("File printed")
        pdfFile.close()
    }

    private fun findPrintService(printerName: String): PrintService? {
        val services = PrintServiceLookup.lookupPrintServices(null, null)
        for (service in services) {
            logger.info("Looking up print service ${service.name}")
            for (attributes in service.attributes.toArray()) {
                logger.info("Attribute: $attributes")
            }
            for (flavor in service.supportedDocFlavors) {
                logger.info("Supported flavor: $flavor")
            }
            if (service.name.equals(printerName, ignoreCase = true)) {

                return service
            }
        }
        return null
    }
}