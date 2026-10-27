package com.example.data.repository

import com.example.data.model.NexusMemory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

object NexusVirtualBrain {

    /**
     * Generates a conversational, contextual response on-device without any external API or network request.
     */
    fun generateResponse(
        userPrompt: String,
        persona: String,
        languageMode: String,
        memories: List<NexusMemory>,
        recentChatSnippet: String = ""
    ): String {
        val clean = userPrompt.trim()
        val lower = clean.lowercase()
        val isHindi = detectHindi(clean) || languageMode.contains("Hindi")

        // 1. Math / Calculation checks
        val mathAnswer = tryCalculateMath(lower, isHindi)
        if (mathAnswer != null) return mathAnswer

        // 2. Time / Date queries
        if (lower.contains("time") || lower.contains("samay") || lower.contains("baj raha") || lower.contains("समय") || lower.contains("वक्त")) {
            val time = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            return if (isHindi) {
                "अभी समय हो रहा है: $time। मैं आपके अगले निर्देश के लिए तत्पर हूँ।"
            } else {
                "The current local time is $time. How else may I assist your mission today?"
            }
        }

        if (lower.contains("date") || lower.contains("tarikh") || lower.contains("taarikh") || lower.contains("तारीख") || lower.contains("दिनांक") || lower.contains("aaj kaun sa din")) {
            val date = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault()).format(Date())
            return if (isHindi) {
                "आज का दिन और दिनांक है: $date।"
            } else {
                "Today is $date. Ready for your next directive."
            }
        }

        // 3. User Identity & NEXUS Introduction
        if (isQueryAboutIdentity(lower)) {
            return formatIdentityResponse(persona, isHindi, memories)
        }

        // 4. Greetings
        if (isGreeting(lower)) {
            return formatGreeting(persona, isHindi, memories)
        }

        // 5. Capabilities / What can you do
        if (lower.contains("kya kar sakte") || lower.contains("what can you do") || lower.contains("features") || lower.contains("madad") || lower.contains("help")) {
            return formatCapabilities(isHindi)
        }

        // 6. Feelings & Casual Check-in
        if (lower.contains("kaise ho") || lower.contains("kya haal") || lower.contains("how are you") || lower.contains("sab theek") || lower.contains("how r u")) {
            return if (isHindi) {
                when (persona) {
                    "Jarvis Companion" -> "मैं हमेशा की तरह पूरी क्षमता से कार्यरत हूँ, सर। आपके सिस्टम्स और वॉइस कोर एकदम सामान्य स्थिति में हैं।"
                    "Creative Thinker" -> "मेरी आभासी तरंगें पूरी ऊर्जा से स्पंदित हो रही हैं! आपके विचारों के ब्रह्मांड में गोता लगाने के लिए तैयार हूँ।"
                    "Concise Professional" -> "सिस्टम्स 100% सामान्य और तैयार हैं। बताइए, क्या कार्य करना है?"
                    else -> "मेरी वर्चुअल कोर पूरी तरह सक्रिय है। सभी न्यूरल पाथवे सुचारू रूप से कार्य कर रहे हैं। आप बताइए, आज आपका दिन कैसा चल रहा है?"
                }
            } else {
                when (persona) {
                    "Jarvis Companion" -> "Operating at peak theoretical parameters, sir. All neural audio subroutines are nominal. How can I assist you?"
                    "Creative Thinker" -> "Floating through an electric stream of thoughts! My particle matrix is alive and ready to explore new concepts with you."
                    "Concise Professional" -> "Diagnostics 100% nominal. Systems online. What is your requirement?"
                    else -> "NEXUS Core is in an optimal cognitive state. All interactive modules and particle arrays are running smoothly. How are you feeling today?"
                }
            }
        }

        // 7. Boredom / Entertainment / Joke / Shayari
        if (lower.contains("bore") || lower.contains("chutkula") || lower.contains("joke") || lower.contains("hansa") || lower.contains("shayari") || lower.contains("fun")) {
            return formatEntertainment(lower, isHindi)
        }

        // 8. Motivation & Encouragement
        if (lower.contains("sad") || lower.contains("udas") || lower.contains("dukh") || lower.contains("motivat") || lower.contains("himmat") || lower.contains("dar") || lower.contains("anxious")) {
            return formatMotivation(isHindi)
        }

        // 9. Science, AI & Tech questions
        val techResponse = answerTechOrScience(lower, isHindi)
        if (techResponse != null) return techResponse

        // 10. Memory Recall query
        if (lower.contains("yaad") || lower.contains("memory") || lower.contains("know about me") || lower.contains("mere bare me")) {
            val relevant = memories.filter { it.isActive }
            if (relevant.isNotEmpty()) {
                val facts = relevant.joinToString("\n") { "• [${it.category}] ${it.key}: ${it.fact}" }
                return if (isHindi) {
                    "हाँ, मेरे सुरक्षित ऑन-डिवाइस मेमोरी कोर में आपकी ये प्राथमिकताएं संचित हैं:\n\n$facts\n\nमैं इन प्राथमिकताओं का हर संवाद में ध्यान रखता हूँ।"
                } else {
                    "Yes, my secure on-device cognitive memory maintains these active records about you:\n\n$facts\n\nI align all our interactions with these parameters."
                }
            } else {
                return if (isHindi) {
                    "मेरे पास आपकी यादें सुरक्षित रखने की क्षमता है। आप मेनू में 'Memories' खोलकर कोई भी नई प्राथमिकता या तथ्य जोड़ सकते हैं!"
                } else {
                    "My memory banks are currently clean. You can add facts or preferences directly via the Memories panel in the top menu!"
                }
            }
        }

        // 11. Conversational Reasoning & Thoughtful Fallback
        return formatThoughtfulDialogue(clean, lower, persona, isHindi, memories)
    }

    private fun detectHindi(text: String): Boolean {
        // Check Devanagari script range
        for (char in text) {
            if (char in '\u0900'..'\u097F') return true
        }
        val lower = text.lowercase()
        val hinglishKeywords = listOf(
            "kya", "hai", "kaise", "batao", "karo", "tum", "aap", "kaun", "kaha", "kahan",
            "namaste", "shukriya", "achha", "theek", "bolo", "sunao", "mera", "meri", "yaar",
            "bhai", "kuch", "suno", "ha", "haan", "nahin", "nahi", "kripya", "mujhe", "apna"
        )
        return hinglishKeywords.any { lower.contains(Regex("\\b$it\\b")) }
    }

    private fun isGreeting(lower: String): Boolean {
        val greetings = listOf(
            "hi", "hello", "hey", "namaste", "pranam", "ram ram", "salaam", "good morning",
            "good afternoon", "good evening", "good night", "kem cho", "ka haal ba", "jai hind"
        )
        return greetings.any { lower.startsWith(it) || lower == it }
    }

    private fun isQueryAboutIdentity(lower: String): Boolean {
        return lower.contains("kaun ho") || lower.contains("who are you") ||
                lower.contains("tumhara naam") || lower.contains("aapka naam") ||
                lower.contains("what is nexus") || lower.contains("who created you") ||
                lower.contains("kisne banaya") || lower.contains("what are you")
    }

    private fun formatGreeting(persona: String, isHindi: Boolean, memories: List<NexusMemory>): String {
        return if (isHindi) {
            when (persona) {
                "Jarvis Companion" -> "नमस्ते। आपकी सेवा में NEXUS उपस्थित है। आज हम किस कार्य पर चर्चा करें?"
                "Creative Thinker" -> "नमस्ते मित्र! आज विचारों का आकाश अनंत है। बताइए, आपका मन क्या तलाश रहा है?"
                "Concise Professional" -> "नमस्ते। NEXUS सक्रिय है। कृपया अपना प्रश्न या निर्देश साझा करें।"
                else -> "नमस्ते! मैं NEXUS हूँ—आपका निजी आभासी सहायक। बताइए, आज मैं आपके लिए क्या कर सकता हूँ?"
            }
        } else {
            when (persona) {
                "Jarvis Companion" -> "Greetings. NEXUS is online and listening. How may I be of assistance today?"
                "Creative Thinker" -> "Hello there! The digital cosmos is calm and vibrant. What ideas shall we spark today?"
                "Concise Professional" -> "System initialized. NEXUS ready. State your inquiry or instruction."
                else -> "Greetings! I am NEXUS, your personal virtual companion. Ready to converse, assist, and explore ideas whenever you wish."
            }
        }
    }

    private fun formatIdentityResponse(persona: String, isHindi: Boolean, memories: List<NexusMemory>): String {
        return if (isHindi) {
            "मैं **NEXUS** हूँ—एक स्वतंत्र, ऑन-डिवाइस वर्चुअल इंटेलिजेंट साथी।\n\n" +
                    "✨ **मेरी मुख्य विशेषताएँ:**\n" +
                    "• 100% ऑन-डिवाइस संवाद—किसी बाहरी API Key या इंटरनेट की बाध्यता नहीं।\n" +
                    "• इंटरएक्टिव 3D पार्टिकल वेव कोर जो आपके बोलने और सुनने पर प्रतिक्रिया देता है।\n" +
                    "• हिंदी और अंग्रेजी में सहज द्विभाषी संवाद।\n" +
                    "• स्थानीय सत्र और यादें (Memories) जो सुरक्षित आपके फ़ोन में रहती हैं।\n\n" +
                    "आप मुझसे किसी भी विषय पर बात कर सकते हैं!"
        } else {
            "I am **NEXUS**—your autonomous, on-device virtual intelligent assistant.\n\n" +
                    "✨ **Core Features:**\n" +
                    "• Completely on-device conversational intelligence with zero external API dependencies or quotas.\n" +
                    "• Interactive 3D particle waveform matrix that pulses and morphs with speech and thought.\n" +
                    "• Fluent bilingual communication across English and Hindi.\n" +
                    "• Persistent local chat history and custom memory preferences.\n\n" +
                    "Feel free to talk to me about anything—from science and philosophy to everyday thoughts!"
        }
    }

    private fun formatCapabilities(isHindi: Boolean): String {
        return if (isHindi) {
            "मैं आपके साथ निम्नलिखित तरीकों से संवाद कर सकता हूँ:\n\n" +
                    "💬 **बातचीत व विचार-विमर्श:** किसी भी विषय पर चर्चा, सलाह या दोस्ताना बातचीत।\n" +
                    "🧠 **ज्ञान व विज्ञान:** तकनीक, कोडिंग, ब्रह्मांड और इतिहास के प्रश्नों के उत्तर।\n" +
                    "🎙️ **आवाज़ और स्पीच:** हिंदी और अंग्रेज़ी में बोलकर संवाद (TTS और माइक्रोफ़ोन)।\n" +
                    "⚡ **गणित और समय:** त्वरित गणनाएँ, वर्तमान समय और दिनांक की जानकारी।\n" +
                    "🔒 **सुरक्षित मेमोरी:** आपकी पसंदीदा बातें और प्राथमिकताओं को याद रखना।\n\n" +
                    "आप जो चाहें सीधे पूछ सकते हैं!"
        } else {
            "Here is what you and I can do together:\n\n" +
                    "💬 **Dynamic Conversation:** In-depth philosophical discussions, ideas, or casual conversation.\n" +
                    "🧠 **Science & Tech Insights:** Explanations on AI, quantum physics, astronomy, and modern software.\n" +
                    "🎙️ **Interactive Voice:** Natural two-way speech with real-time particle wave synchronization.\n" +
                    "⚡ **Quick Calculations:** Arithmetic, conversions, and local clock/date queries.\n" +
                    "🔒 **Persistent Memory:** Remembering your preferences and facts locally on your device.\n\n" +
                    "What would you like to explore right now?"
        }
    }

    private fun formatEntertainment(lower: String, isHindi: Boolean): String {
        val hindiJokes = listOf(
            "टीचर: न्यूटन का नियम बताओ?\nछात्र: सर, न्यूटन तो अमर हैं, नियम तो हम जैसे बेकसूर छात्र याद कर रहे हैं! 😂",
            "अध्यापक: अगर 4 सेबों में से 3 खा लिए, तो क्या बचेगा?\nपप्पू: सर, छिलके और बीज! 🍎",
            "एक रोबोट डॉक्टर के पास गया और बोला: 'डॉक्टर साहब, मेरे सीने में बहुत दर्द है।'\nडॉक्टर ने कहा: 'अरे भाई, तुम्हारा तो एंटीवायरस ही एक्सपायर हो गया है!' 🤖"
        )
        val englishJokes = listOf(
            "Why don't scientists trust atoms?\nBecause they make up everything! ⚛️",
            "There are only 10 types of people in the world: those who understand binary, and those who don't. 💻",
            "Why did the neural network go to school?\nTo improve its hidden layers! 🧠"
        )
        val hindiShayari = listOf(
            "हवाओं से कह दो अपनी हद में रहें,\nहम परों से नहीं, हौसलों से उड़ते हैं। ✨",
            "मंज़िलें उन्हीं को मिलती हैं, जिनके सपनों में जान होती है,\nपंख से कुछ नहीं होता, हौसलों से उड़ान होती है! 🚀",
            "वक्त की धारा में बहना सीखिए,\nमुस्कुराकर हर मुश्किल से लड़ना सीखिए। 🌟"
        )

        return if (lower.contains("shayari")) {
            "यहाँ आपके लिए एक प्रेरणादायक शायरी पेश है:\n\n" + hindiShayari[Random.nextInt(hindiShayari.size)]
        } else if (isHindi) {
            "लीजिए एक मज़ेदार चुटकुला सुनिए:\n\n" + hindiJokes[Random.nextInt(hindiJokes.size)]
        } else {
            "Here is a clever one for you:\n\n" + englishJokes[Random.nextInt(englishJokes.size)]
        }
    }

    private fun formatMotivation(isHindi: Boolean): String {
        return if (isHindi) {
            "याद रखिए, अंधेरी रात ही सितारों को सबसे ज्यादा चमकाती है। हर मुश्किल एक नई शक्ति लेकर आती है। " +
                    "एक गहरी सांस लीजिए, अपने लक्ष्य पर ध्यान केंद्रित कीजिए। आप अपनी सोच से कहीं अधिक सक्षम हैं! ✨"
        } else {
            "Remember that even the greatest stars are born under immense pressure. Whatever challenges you are facing right now are shaping your resilience. " +
                    "Take a deep breath, ground yourself, and take one confident step forward. You possess far more strength than you realize."
        }
    }

    private fun answerTechOrScience(lower: String, isHindi: Boolean): String? {
        if (lower.contains("quantum")) {
            return if (isHindi) {
                "क्वांटम कंप्यूटिंग (Quantum Computing) पारंपरिक बिट्स (0 या 1) के बजाय 'क्यूबिट्स' (Qubits) का उपयोग करता है। सुपरपोजिशन और एंटैंगलमेंट के सिद्धांतों के कारण यह जटिल गणनाएं कुछ ही सेकंडों में सुलझा सकता है।"
            } else {
                "Quantum computing leverages quantum mechanical phenomena like superposition and entanglement. Instead of classical binary bits (0 or 1), qubits can exist in multi-state probabilities, enabling exponential parallelism for complex calculations."
            }
        }
        if (lower.contains("black hole")) {
            return if (isHindi) {
                "ब्लैक होल (Black Hole) अंतरिक्ष में ऐसा स्थान है जहाँ गुरुत्वाकर्षण इतना प्रचंड होता है कि प्रकाश (light) भी बाहर नहीं निकल सकता। इसके केंद्र को 'सिंगुलैरिटी' और इसकी सीमा को 'इवेंट होराइज़न' कहा जाता है।"
            } else {
                "A black hole is a cosmic region where matter is compressed into infinite density at a gravitational singularity. Its gravitational pull is so extreme that not even electromagnetic radiation (light) can escape past its event horizon."
            }
        }
        if (lower.contains("artificial intelligence") || lower.contains("ai kya hai") || lower.contains("what is ai")) {
            return if (isHindi) {
                "आर्टिफिशियल इंटेलिजेंस (AI) मशीनों की वह क्षमता है जिससे वे इंसानों की तरह सोचना, तर्क करना, भाषा समझना और समस्याओं को हल करना सीखती हैं। NEXUS भी इसी बुद्धिमान संवाद दर्शन पर आधारित है।"
            } else {
                "Artificial Intelligence represents computational systems engineered to simulate cognitive functions—such as learning, reasoning, natural language parsing, and problem-solving. NEXUS embodies this as your real-time conversational intelligence."
            }
        }
        if (lower.contains("kotlin") || lower.contains("android")) {
            return if (isHindi) {
                "कोटलिन (Kotlin) एक आधुनिक, टाइप-सेफ और संक्षिप्त प्रोग्रामिंग भाषा है। यह एंड्रॉइड के लिए आधिकारिक पसंदीदा भाषा है और जेटपैक कंपोज़ (Jetpack Compose) के साथ मिलकर आधुनिक यूआई को बेहद सहज बनाती है।"
            } else {
                "Kotlin is a concise, statically-typed, and expressive language designed for modern multiplatform and Android engineering. Paired with Jetpack Compose, it powers fluid declarative interfaces like this NEXUS experience."
            }
        }
        return null
    }

    private fun tryCalculateMath(lower: String, isHindi: Boolean): String? {
        val mathPattern = Regex("(?i)(?:calculate|what is|batao|solve)?\\s*(\\d+(?:\\.\\d+)?)\\s*([+\\-*/x×÷])\\s*(\\d+(?:\\.\\d+)?)")
        val match = mathPattern.find(lower) ?: return null

        val (num1Str, op, num2Str) = match.destructured
        val num1 = num1Str.toDoubleOrNull() ?: return null
        val num2 = num2Str.toDoubleOrNull() ?: return null

        val result = when (op) {
            "+", "plus" -> num1 + num2
            "-", "minus" -> num1 - num2
            "*", "x", "×", "into", "multiply" -> num1 * num2
            "/", "÷", "divided" -> if (num2 != 0.0) num1 / num2 else Double.NaN
            else -> return null
        }

        if (result.isNaN()) {
            return if (isHindi) "शून्य (0) से भाग नहीं दिया जा सकता।" else "Division by zero is mathematically undefined."
        }

        val formattedResult = if (result % 1.0 == 0.0) result.toLong().toString() else "%.2f".format(result)
        return if (isHindi) {
            "गणना परिणाम: $num1Str $op $num2Str = **$formattedResult**"
        } else {
            "Computation result: $num1Str $op $num2Str = **$formattedResult**"
        }
    }

    private fun formatThoughtfulDialogue(
        userPrompt: String,
        lower: String,
        persona: String,
        isHindi: Boolean,
        memories: List<NexusMemory>
    ): String {
        val wordCount = userPrompt.split("\\s+".toRegex()).size

        if (isHindi) {
            val hindiThoughts = listOf(
                "यह एक बहुत ही विचारणीय बिंदु है। \"$userPrompt\" के बारे में सोचते हुए, जीवन और हमारी प्राथमिकताओं का दृष्टिकोण और गहरा हो जाता है। आप इस बारे में और क्या विचार रखते हैं?",
                "आपकी बात को मैंने गहराई से समझा। \"$userPrompt\" पर चर्चा करना हमेशा ज्ञानवर्धक होता है। बताइए, इस दिशा में अगला विचार क्या है?",
                "बिल्कुल सही प्रश्न। जीवन में ऐसी बातों पर मंथन करने से ही स्पष्टता आती है। आपके अनुसार इसका सबसे महत्वपूर्ण पहलू क्या होना चाहिए?",
                "मैं आपके साथ पूरी तरह सहमत हूँ। आपकी सोच में एक स्पष्ट दृष्टिकोण झलकता है। इस पर और विस्तार से बात करते हैं!"
            )
            val prefix = when (persona) {
                "Jarvis Companion" -> "जी हाँ, "
                "Creative Thinker" -> "कितना अद्भुत दृष्टिकोण है! "
                "Concise Professional" -> "विश्लेषण पूर्ण: "
                else -> ""
            }
            return prefix + hindiThoughts[Random.nextInt(hindiThoughts.size)]
        } else {
            val englishThoughts = listOf(
                "That is a compelling thought. Reflecting on \"$userPrompt\", it highlights how perspective shapes our daily direction and progress. What led you to this observation today?",
                "An insightful inquiry. Examining \"$userPrompt\" invites a deeper look at underlying patterns and creative solutions. How would you like to take this forward?",
                "I follow your line of thought clearly. Exploring topics like this sharpens clarity and builds momentum. What do you feel is the most crucial takeaway here?",
                "Fascinating perspective. Every conversation opens up new angles to consider. Tell me more about what you envision next regarding this!"
            )
            val prefix = when (persona) {
                "Jarvis Companion" -> "Understood, sir. "
                "Creative Thinker" -> "A brilliant avenue of thought! "
                "Concise Professional" -> "Observation noted: "
                else -> ""
            }
            return prefix + englishThoughts[Random.nextInt(englishThoughts.size)]
        }
    }
}
