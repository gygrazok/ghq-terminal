package com.terredegliangeli.ghq.terminal.mqtt.handlers

import com.fasterxml.jackson.databind.ObjectMapper
import com.terredegliangeli.ghq.terminal.mqtt.dto.StandardMessage
import com.terredegliangeli.ghq.terminal.mqtt.dto.VideotelMessage
import com.terredegliangeli.ghq.terminal.service.CommunicationService
import com.terredegliangeli.ghq.terminal.service.FilePrintService
import org.springframework.stereotype.Component

@Component
class VideotelStandardMessageHandler(
    private val communicationService: CommunicationService,
    override val objectMapper: ObjectMapper,
    private val printService: FilePrintService
) :
    AbstractMqttMessageHandler<StandardMessage>() {
    override val messageType = StandardMessage::class.java
    override val topic: String = "ghq/standardMessage"
    override fun onMessageArrived(message: StandardMessage) {
        if (message.key == "barbero") {
            communicationService.receiveMessage(VideotelMessage(barbero))
            printService.printFile("barbero.pdf")
        }
        if (message.key == "ivo") {
            communicationService.receiveMessage(VideotelMessage(ivo))
        }
    }

}

val barbero = """
    Traduzione automatica in base a impostazione lingua locale
    Messaggio GHQ:
    Attenzione aspiranti Ghostbusters,
    Il nostro HQ ha individuato un soggetto che risponde al nome di Prof.
    Bonifacio Crippa, il quale pochi anni fa era al centro di uno scandalo
    legato a presunte pratiche mistiche.
    Costui ha compiuto in segreto numerose visite presso Larniano nel corso
    degli ultimi 3 anni e riteniamo che possa avere pilotato la scelta della
    sede per il nostro evento.
    Purtroppo, abbiamo poche notizie sul suo conto ma di recente ha richiesto
    numerose copie di un saggio storico chiamato "Due Secoli di Perle",
    realizzato da un giovane accademico torinese chiamato Alessandro Barbero.
    Vi mandiamo in stampa quel documento nella speranza che possa esservi
    utile!
    Peter
    PS. Prendete il numero delle candidate più belle!
""".trimIndent()

val ivo = """
    Traduzione automatica in base a impostazione lingua locale
    Messaggio GHQ:
    Ragazzi, qua sono cavoli amari!
    Egon ha scritto dieci pagine di appunti ed è grassa se sono riuscito ad
    afferrare quattro frasi. Ray balbetta come un moccioso che ha visto
    l’uomo nero e neanche con due ceffoni è riuscito a recuperare la calma.
    Tocca ancora al vecchio Peter fare il punto.
    Pare che sia saltato fuori il nome di Ivo Shandor, quel pazzo bastardo
    che ha costruito il palazzo a 55 Central Park West; sì, proprio quello
    dove abbiamo rotto il culo a quella brutta stronza di Gozer.
    Ray ha scoperto che Ivo non è sempre rimasto negli Stati Uniti. Nel 1917
    quel figlio di puttana si è arruolato come medico nell’esercito alleato
    che combatteva assieme alle truppe britanniche sul fronte francese. In
    seguito, è rimasto per un bel pezzo lì in Europa e il suo ritorno in
    America è avvenuto solo nel 1943.
    Ora veniamo alle cose peggiori. Egon ha scovato una ricevuta telegrafica
    del 1917, risalente a una settimana prima della partenza di Ivo per la
    Francia. Il nostro amico ha trovato modo d’inviare un messaggio a una
    radioscrivente italiana ed ecco cosa aveva scritto:
    
    STO-ARRIVANDO-PREPARATE-UN-GRUPPO-PRESSO-LA-PORTA-DEGLI-INFERI-RICEVERA-
    APPENA-POSSIBILE-MIE-NOTIZIE-IL-LUOGO-SI-CHIAMA-LARNIANO
    
    Peter
    PS. Se dovesse piovere merda là dove siete, non fate gli eroi, anzi,
    fatelo solo se siete davvero MOLTO convinti di esserlo!
""".trimIndent()