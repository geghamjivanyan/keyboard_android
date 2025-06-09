package com.phonemics.keyboard.data

class TransformationEngine {
    
    private val dotTransformations = mapOf(
        // أ -> إ -> ئ -> ؤ (hamza cycle)
        "\u0621" to "\u0623", // ء -> أ
        "\u0623" to "\u0625", // أ -> إ
        "\u0625" to "\u0626", // إ -> ئ
        "\u0626" to "\u0624", // ئ -> ؤ
        "\u0624" to "\u0623", // ؤ -> أ
        
        // ح -> خ -> ج -> ح (3 dots cycle)
        "\u062D" to "\u062E", // ح -> خ
        "\u062E" to "\u062C", // خ -> ج
        "\u062C" to "\u062D", // ج -> ح
        
        // ب (dotless) -> ب -> ت -> ث -> ب (dotless)
        "\u066E" to "\u0628", // ٮ -> ب
        "\u0628" to "\u062A", // ب -> ت
        "\u062A" to "\u062B", // ت -> ث
        "\u062B" to "\u066E", // ث -> ٮ
        
        // ه -> ة -> ه
        "\u0647" to "\u0629", // ه -> ة
        "\u0629" to "\u0647", // ة -> ه
        
        // ص -> ض -> ص
        "\u0635" to "\u0636", // ص -> ض
        "\u0636" to "\u0635", // ض -> ص
        
        // ع -> غ -> ع
        "\u0639" to "\u063A", // ع -> غ
        "\u063A" to "\u0639", // غ -> ع
        
        // ن -> ت
        "\u0646" to "\u062A", // ن -> ت
        
        // ف -> ق -> ف
        "\u0641" to "\u0642", // ف -> ق
        "\u0642" to "\u0641", // ق -> ف
        
        // ر -> ز -> ر
        "\u0631" to "\u0632", // ر -> ز
        "\u0632" to "\u0631", // ز -> ر
        
        // د -> ذ -> د
        "\u062F" to "\u0630", // د -> ذ
        "\u0630" to "\u062F", // ذ -> د
        
        // ط -> ظ -> ط
        "\u0637" to "\u0638", // ط -> ظ
        "\u0638" to "\u0637", // ظ -> ط
        
        // س -> ش -> س
        "\u0633" to "\u0634", // س -> ش
        "\u0634" to "\u0633", // ش -> س
        
        // Vowel transformations
        "\u064E" to "\u064B", // فتح -> تنوين فتح
        "\u064F" to "\u064C", // ضم -> تنوين ضم
        "\u0650" to "\u064D", // كسر -> تنوين كسر
        
        // Characters that don't change
        "\u064A" to "\u064A", // ي -> ي
        "\u0644" to "\u0644", // ل -> ل
        "\u0645" to "\u0645", // م -> م
        "\u0643" to "\u0643", // ك -> ك
        "\u0648" to "\u0648", // و -> و
        "\u0627" to "\u0649"  // ا -> ى
    )
    
    private val arabicOrthographicRules = listOf(
        // Diacritic transformations
        TransformationRule("ـ", " "),
        TransformationRule(" ً", " أَن"),
        TransformationRule(" ٌ", " أُن"),
        TransformationRule(" ٍ", " إِن"),
        TransformationRule(" ءَ", " أَ"),
        TransformationRule(" ءُ", " أُ"),
        TransformationRule(" ءِ", " إِ"),
        TransformationRule("أََ", "آ"),
        TransformationRule("ُُ", "ُو"),
        TransformationRule("ِِ", "ِي"),
        TransformationRule(" ُ", " و"),
        TransformationRule(" ِ", " ي"),
        TransformationRule("الءَ", "الأَ"),
        TransformationRule("الءُ", "الأُ"),
        TransformationRule("يِ", "يّ"),
        TransformationRule("يِّ", "يِّ"),
        TransformationRule("يِِّ", "يِّي"),
        TransformationRule("وُ", "وّ"),
        TransformationRule("وُّ", "وُّو"),
        TransformationRule(" الُ", " الو"),
        TransformationRule(" الِ", " الي"),
        TransformationRule(" الل", " الل"),
        TransformationRule("ََ", "َا"),
        TransformationRule(" ُُ", " ُو"),
        TransformationRule(" ِِ", " ِي"),
        TransformationRule("َُ", "َو"),
        TransformationRule("َِ", "َي"),
        TransformationRule("َُ", "وَ"),
        TransformationRule("ُِ", "وِ"),
        TransformationRule("ُُِ", "ُيُ"),
        TransformationRule("َِ", "يَ"),
        TransformationRule("ُِ", "يُ"),
        TransformationRule("ُِِ", "ِوِ"),
        TransformationRule("اُ", "ٌ "),
        TransformationRule("َُ", "او"),
        TransformationRule("اِ", "اي"),
        TransformationRule("ِيَ", "ِيَ"),
        TransformationRule("ِيُ", "ِيُ"),
        TransformationRule("ُوَ", "ُوَ"),
        TransformationRule("ُوِ", "ُوِ"),
        TransformationRule("َِ", "يَ"),
        TransformationRule("ُِ", "يُ"),
        TransformationRule("ؤِ", "ئِ"),
        TransformationRule("اءََ", "اءَا"),
        TransformationRule("يء", "ئ"),
        TransformationRule("وءََ", "وءَا"),
        TransformationRule("اءُ ", "اءُ "),
        TransformationRule("اءِ ", "اءِ "),
        
        // Hamza transformations
        TransformationRule(" ءَ", " أَ"),
        TransformationRule(" ءِ", " إ"),
        TransformationRule(" ءُ", " أُ"),
        TransformationRule("ِء", "ِئ"),
        TransformationRule("ءِ", "ئِ"),
        TransformationRule("ءُ", "ؤُ"),
        TransformationRule("ُء", "ُؤ"),
        TransformationRule("َء", "َأ"),
        TransformationRule("ءَ", "أَ"),
        TransformationRule("ءََ", "ءا"),
        TransformationRule("ءََ", "آ"),
        
        // Complex prefix rules
        TransformationRule(" فَؤُ", " فَأُ"),
        TransformationRule(" فَئِ", " فَإِ"),
        TransformationRule(" كَؤُ", " كَأُ"),
        TransformationRule(" كَئِ", " كَإِ"),
        TransformationRule("كَإِيب", "كَئيب"),
        TransformationRule(" وَؤُ", " وَأُ"),
        TransformationRule(" وَئِ", " وَإِ"),
        TransformationRule("بِئِ", "بإِ"),
        TransformationRule("بِئَ", "بِأَ"),
        TransformationRule("بِئُ", "بِأُ"),
        TransformationRule("الؤُ", "الأُ"),
        TransformationRule("الئِ", "الإِ"),
        TransformationRule("لِئِ", "لِأَ"),
        TransformationRule("لِئُ", "لِأُ"),
        TransformationRule("لِلئِ", "لِلإِ"),
        TransformationRule("لِلؤُ", "لِلأُ"),
        TransformationRule("لِئِ", "لِإِ"),
        TransformationRule("أَئِ", "أَإِ"),
        TransformationRule("أَؤُ", "أَأُ"),
        TransformationRule(" لَِ", " لِا"),
        
        // Names and suffixes
        TransformationRule("عَبدَل", "عَبدَال"),
        TransformationRule("عَبدِل", "عَبدِال"),
        TransformationRule("عَبدُل", "عَبدُال"),
        TransformationRule("عَبدَالءِ", "عَبدَالإِ"),
        TransformationRule("عَبدَالءُ", "عَبدَالأُ"),
        
        // Trailing rules
        TransformationRule("حَتَّا ", "حَتَّى "),
        TransformationRule("عَلا ", "عَلَى"),
        TransformationRule("بَلا ", "بَلَى"),
        TransformationRule("إلا ", "إِلَى "),
        TransformationRule("مَتا ", "مَتَى"),
        TransformationRule("عيسا ", "عِيسَى "),
        TransformationRule("موسا ", "مُوسَى "),
        TransformationRule("لَدا ", " لَدَى "),
        TransformationRule("أولا ", "أَولَى "),
        TransformationRule("رَعا ", "رَعَى "),
        TransformationRule("رَما ", "رَمَى "),
        TransformationRule("اءً ", "وءا"),
        TransformationRule("أً ", "أً "),
        TransformationRule("ةً ", "ةً "),
        TransformationRule("اً ", "اً "),
        TransformationRule("ىً ", "ىً "),
        TransformationRule(" َ", " ا"),
        TransformationRule("ً", "ً "),
        TransformationRule("ٍ", "ٍ "),
        TransformationRule("ٌ", "ٌ "),
        TransformationRule("ً ّا", "ّاً "),
        TransformationRule("ً ا", "اً "),
        TransformationRule("  ", ", "),
        TransformationRule(",,", ". "),
        TransformationRule("اللَاه", "اللّه"),
        TransformationRule("هَاأَنتُم", "هَأَنتُم"),
        TransformationRule("هَاأَنَا", "هَأَنَا"),
        TransformationRule("هَاذِهِ", "هَذِهِ"),
        TransformationRule("هَاؤُلَاء", "هَؤُلَاء"),
        TransformationRule("ذَالِك", "ذَلِك"),
        TransformationRule("هَاذَا", "هَذَا"),
        TransformationRule("لَاكِن", "لَكِن"),
        TransformationRule("إِلَاه", "إِلَه"),
        TransformationRule("الرَّحمَان", "الرَّحمَن"),
        TransformationRule("أُلِي", "أُولِي"),
        TransformationRule("أُلُو", "أُولُو"),
        TransformationRule("أُلَائِك", "أُولَئِك"),
        TransformationRule("عَمرُ", "عَمرو")
    )
    
    // Add double consonant rules programmatically
    init {
        val emphatics = listOf("ض", "ص", "ث", "د", "ش", "س", "ت", "ن", "ط", "ر", "ز", "ظ", "ذ")
        val allConsonants = listOf(
            "ق", "ف", "غ", "ع", "ت", "ث", "د", "ذ", "ش", "ه", "خ", "ح", "ج", 
            "س", "ص", "ط", "ظ", "ن", "ض", "ي", "ب", "ل", "م", "ك", "و"
        )
        val prefixConsonants = listOf(
            "ق", "ف", "غ", "ل", "ع", "ه", "خ", "ح", "ج", "ي", "ب", "م", "ك", "ء", "و"
        )
        
        // Add double consonant transformations for emphatics (space + double letter)
        val doubleEmphatics = emphatics.map { letter ->
            TransformationRule(" $letter$letter", if (letter == "ل") " الل" else " ال${letter}ّ")
        }
        
        // Add double consonant transformations for all consonants (no space)
        val doubleConsonants = allConsonants.map { letter ->
            TransformationRule("$letter$letter", "${letter}ّ")
        }
        
        // Add prefix transformations with ل
        val prefixTransformations = prefixConsonants.map { letter ->
            TransformationRule(" ل$letter", " ال$letter")
        }
        
        arabicOrthographicRules.toMutableList().apply {
            addAll(doubleEmphatics)
            addAll(doubleConsonants)
            addAll(prefixTransformations)
        }
    }
    
    fun transform(text: String): String {
        var transformed = text
        arabicOrthographicRules.forEach { rule ->
            transformed = transformed.replace(rule.pattern, rule.replacement)
        }
        return transformed
    }
    
    fun getDotTransformation(character: String): String {
        return dotTransformations[character] ?: character
    }
    
    private data class TransformationRule(
        val pattern: String,
        val replacement: String
    )
}