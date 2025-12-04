package com.example.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpService {

    private final List<HelpTopic> topics = new ArrayList<>();

    public HelpService() {
        seedData();
    }

    private void seedData() {
        // ---------- TØJVASK ----------
        topics.add(new HelpTopic(
                1,
                "Vaske hvidt tøj",
                HelpCategory.LAUNDRY,
                "Sådan vasker du hvidt tøj:\n" +
                        "1) Sortér vasketøjet: Læg KUN hvidt tøj sammen (ingen farver eller sort).\n" +
                        "2) Tjek vaskeanvisningen i tøjet (lille label inde i tøjet) – den viser maks. temperatur.\n" +
                        "3) Som udgangspunkt kan almindeligt hvidt bomuldstøj vaskes ved 60°.\n" +
                        "4) Sart hvidt tøj (sportstøj, uld, silke) vaskes ofte ved 30–40°.\n" +
                        "5) Brug hvidt vaskemiddel (uden farve) – ikke skyllemiddel til håndklæder, det gør dem mindre sugende.\n" +
                        "6) Når vasken er færdig, hæng tøjet op med det samme – hvis det ligger vådt for længe, kan det komme til at lugte surt.\n" +
                        "7) Lad tøjet tørre helt, før du lægger det i skabet.\n" +
                        "\nTIP: Bliver det ved med at lugte, så vask igen – evt. med en forvask eller længere program.",
                Arrays.asList("hvidt tøj", "hvid vask", "vaske hvidt", "60", "vask hvidt")
        ));

        topics.add(new HelpTopic(
                2,
                "Vaske farvet tøj",
                HelpCategory.LAUNDRY,
                "Sådan vasker du farvet tøj:\n" +
                        "1) Sortér tøj i lyse farver og mørke farver.\n" +
                        "2) Tjek vaskeanvisningen i tøjet for maks. temperatur.\n" +
                        "3) De fleste farvede bomulds-trøjer/bukser kan vaskes ved 40°.\n" +
                        "4) Brug vaskemiddel til farvet tøj (color).\n" +
                        "5) Vend jeans og meget mørkt tøj på vrangen.\n" +
                        "6) Hæng tøjet op med det samme efter vask.\n" +
                        "7) Undgå at overfylde maskinen – så bliver tøjet ikke ordentligt rent.",
                Arrays.asList("farvet tøj", "farver", "color vask", "vask farvet")
        ));

        topics.add(new HelpTopic(
                3,
                "Vaske mørkt tøj",
                HelpCategory.LAUNDRY,
                "Sådan vasker du mørkt tøj:\n" +
                        "1) Saml alt mørkt tøj for sig.\n" +
                        "2) Tjek vaskeanvisningen – mange mørke trøjer/bukser er 30–40°.\n" +
                        "3) Brug vaskemiddel til mørkt/farvet tøj.\n" +
                        "4) Vend tøjet på vrangen.\n" +
                        "5) Vask gerne ved 30° for at passe på farverne.\n" +
                        "6) Hæng tøjet op med det samme efter vask.\n" +
                        "7) Undgå at overfylde maskinen.",
                Arrays.asList("mørkt tøj", "sort vask", "vask mørkt", "mørk vask")
        ));

        topics.add(new HelpTopic(
                4,
                "Forklaring af vasketøjs-symboler",
                HelpCategory.LAUNDRY,
                getLaundrySymbolsText(),
                Arrays.asList("symbol", "vaske symbol", "mærke i tøjet", "vaskeanvisning", "piktogram")
        ));

        // ---------- ELEKTRONIK / KØL & FRYS ----------
        topics.add(new HelpTopic(
                5,
                "Temperatur i køleskab og fryser",
                HelpCategory.ELECTRONICS,
                "Anbefalede temperaturer:\n" +
                        "- Køleskab: ca. 2–5 °C (mange sigter efter 4 °C).\n" +
                        "- Fryser: ca. -18 °C.\n" +
                        "\nHvorfor det er vigtigt:\n" +
                        "- For varmt: Maden holder kortere tid og kan blive dårlig hurtigere.\n" +
                        "- For koldt i køleskab: Noget kan fryse til, og du bruger mere strøm.\n" +
                        "\nTIP: Et lille termometer i køleskabet/fryseren hjælper dig med at holde øje.",
                Arrays.asList("køleskab", "fryser", "grader", "temperatur", "køl", "frys")
        ));

        // ---------- INDEN INDFLYTNING ----------
        topics.add(new HelpTopic(
                6,
                "Ting du bør have styr på inden du flytter ind",
                HelpCategory.MOVE_IN,
                "Før du flytter ind, er det en god idé at have styr på:\n" +
                        "1) Lejekontrakt: Læs den igennem (opsigelse, husorden, indflytningssyn).\n" +
                        "2) Forsikringer: Indboforsikring, evt. ulykkes- og ansvarsforsikring.\n" +
                        "3) El og varme: Skal du selv vælge elselskab og aflæse målere?\n" +
                        "4) Internet/TV: Bestil i god tid.\n" +
                        "5) Adresseændring: Meld flytning digitalt.\n" +
                        "6) Boligforening/udlejer: Tilmeld dig app, mail eller beboer-portal, så du får beskeder og regler.\n" +
                        "7) Økonomi: Lav et simpelt budget for husleje, el, internet, mad, transport og lidt opsparing.",
                Arrays.asList("flytte ind", "inden jeg flytter", "kontrakt", "forsikring", "indflytning", "boligforening")
        ));

        // ---------- STARTERPACK ----------
        topics.add(new HelpTopic(
                7,
                "Udflytnings-starterpack til hjemmet",
                HelpCategory.STARTERPACK,
                "Forslag til en udflytnings-starterpack (basis til hjemmet):\n" +
                        "KØKKEN:\n" +
                        "- Gryde, pande, bradepande\n" +
                        "- 2–4 tallerkener, glas, kopper, bestik\n" +
                        "- Skærebræt, kniv, grydeskeer, piskeris\n" +
                        "- Si, opbevaringsbokse, viskestykker\n" +
                        "- Opvaskemiddel, opvaskebørste, karklude, affaldsposer\n" +
                        "\nRENGØRING:\n" +
                        "- Støvsuger eller kost/fejebakke\n" +
                        "- Gulvmoppe + spand\n" +
                        "- Universalrengøring, toiletrens, glasrens\n" +
                        "- Mikrofiberklude, gummihandsker\n" +
                        "\nBAD/VASK:\n" +
                        "- Håndklæder, vaskeklude\n" +
                        "- Vaskemiddel, tøjkurv, tørrestativ\n" +
                        "\nANDRE BASISTING:\n" +
                        "- Seng/sovesofa, dyne, pude, sengetøj\n" +
                        "- Forlængerledninger, opladere, lamper\n" +
                        "- Lille værktøjssæt (skruetrækker, hammer, søm/skruer)\n" +
                        "- Førstehjælps-ting: plaster, smertestillende, desinfektion.",
                Arrays.asList("starterpack", "starter pack", "hvad skal jeg have", "flytte hjemmefra", "basis ting")
        ));

        // ---------- RENGØRINGSRUTINE ----------
        topics.add(new HelpTopic(
                8,
                "Venlig rengøringsrutine",
                HelpCategory.CLEANING,
                "En simpel og venlig rengøringsrutine:\n" +
                        "HVER DAG (5–10 min):\n" +
                        "- Ryd køkkenbordet og tør det af.\n" +
                        "- Skyl tallerkener og vask op/brug opvaskemaskine.\n" +
                        "- Hæng viskestykker og karklude til tørre.\n" +
                        "\n1–2 GANGE OM UGEN:\n" +
                        "- Støvsug de rum du bruger mest.\n" +
                        "- Tør borde og andre flader af.\n" +
                        "\n1 GANG OM UGEN – BADEVÆRELSE:\n" +
                        "- Rengør håndvask og armatur.\n" +
                        "- Rengør toilettet.\n" +
                        "- Tjek bruseniche for kalk/sæbe.\n" +
                        "\n1 GANG OM UGEN – KØKKEN:\n" +
                        "- Tør komfur og låger af.\n" +
                        "- Tør køleskabshylder af hvis der er spild.\n" +
                        "\n1 GANG OM MÅNEDEN:\n" +
                        "- Vask gulve grundigt.\n" +
                        "- Tør paneler og håndtag af.\n" +
                        "\nTIP: Vælg fx søndag som 'lille rengøringsdag', så det aldrig bliver for uoverskueligt.",
                Arrays.asList("rengøring", "støvsuge", "badeværelse", "køkken", "rutine", "plan")
        ));
    }

    private String getLaundrySymbolsText() {
        return "Forklaring af de mest almindelige vasketøjs-symboler:\n" +
                "\n1) Kar med tal (30, 40, 60 osv.): maks. vasketemperatur.\n" +
                "2) Kar med hånd i: kun håndvask.\n" +
                "3) Kar med streg under: skånevask.\n" +
                "4) Trekant: blegning (kryds = ingen blegning).\n" +
                "5) Firkant med cirkel i: tørretumbler (prikker = varme).\n" +
                "6) Strygejern med prikker: hvor varmt du må stryge.\n" +
                "7) Cirkel: kemisk rens (til renseri).\n" +
                "\nEr du i tvivl, så vælg lavere temperatur og skåneprogram.";
    }

    // ---------- Offentlige metoder ----------

    /** Alle emner i en kategori (til menu). */
    public List<HelpTopic> getTopicsByCategory(HelpCategory category) {
        List<HelpTopic> result = new ArrayList<>();
        for (HelpTopic topic : topics) {
            if (topic.getCategory() == category) {
                result.add(topic);
            }
        }
        return result;
    }

    /** Fritekst-søgning. */
    public HelpTopic findAnswer(String query) {
        for (HelpTopic topic : topics) {
            if (topic.matches(query)) {
                return topic;
            }
        }
        return null;
    }

    public String ask(String query) {
        HelpTopic topic = findAnswer(query);
        if (topic == null) {
            return "Jeg kunne desværre ikke finde et svar på det endnu.\n" +
                    "Prøv at formulere spørgsmålet lidt anderledes 😊";
        } else {
            return topic.getAnswerText();
        }
    }
}
