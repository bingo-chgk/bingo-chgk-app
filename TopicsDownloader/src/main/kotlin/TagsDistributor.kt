object TagsDistributor {
    private val topicNameToTag = mapOf(
        "АЛИ" to listOf("Мохамед Али", "Мухамед Али", "Мохаммед Али", "Мухаммед Али"),
        "БОР" to listOf("Нильс Бор"),
        "БИРС" to listOf("Амброз Бирс", "Случай на мосту через совиный ручей", "Словарь Сатаны"),
        "МУХА" to listOf("Альфонс Муха"),
        "РУСТ" to listOf("Матиас Руст"),
        "ПЛАТ" to listOf("Сильвия Плат", "Под стеклянным колпаком", "Исповедальная поэзия"),
        "ЗАДАЧА ТРЁХ ТЕЛ" to listOf("задача трех тел", "задача трёх тел", "три тела"),
        "ВЕРЕСКОВЫЙ МЁД" to listOf("вересковый мед"),
        "РОЗУЭЛЛЬСКИЙ ИНЦИДЕНТ" to listOf("Розуэлл", "Розуэлльский"),
        "HELLO, WORLD!" to listOf("hello world"),
        "КРОВЬ, ПОТ И СЛЁЗЫ" to listOf("кровь пот слезы"),
        "ЧЁРНЫЙ ТЮЛЬПАН" to listOf("черный тюльпан"),
        "То́ска".toUpperCase() to listOf("опера тоска", "Тоска Пуччини", "премьера тоски", "Каварадосси", "Флория Тоска"),
        "СИНДРОМ ДЖЕНОВЕЗЕ" to listOf("дженовезе"),
        "В САНТЬЯГО ИДЁТ ДОЖДЬ" to listOf("В Сантьяго Идет Дождь"),
        "ФОНЕТИЧЕСКИЙ АЛФАВИТ ICAO" to listOf("фонетический алфавит"),
        "КРИСТИ" to listOf("Агата Кристи"),
        "ПЛАВИЛЬНЫЙ КОТЁЛ" to listOf("Плавильный Котел"),
        "МАНУСКРИПТ ВОЙНИЧА" to listOf("войнич"),
        "АОКИГАХАРА" to listOf("Аокигахара", "лес самоубийц"),
        "ЭФФЕКТ ЗЛОВЕЩЕЙ ДОЛИНЫ" to listOf("зловещая долина", "эффект зловещей долины"),
        "САЛЕМСКИЕ ВЕДЬМЫ" to listOf("Салем"),
        "ПРАЖСКИЕ ДЕФЕНЕСТРАЦИИ" to listOf("дефенестрации"),
        "БОМБАРДИРОВКА ДРЕЗДЕНА" to listOf("бомбардировка дрездена", "крестовый поход детей", "бойня номер пять"),
        "ПРОКЛЯТИЕ ДЕВЯТОЙ СИМФОНИИ" to listOf("девятая симфония", "проклятие девятой", "десятая симфония"),
        "МИФ О ПЕЩЕРЕ" to listOf("Пещера платона", "платонова пещера", "миф о пещере"),
        "ЗОЛОТЫЕ ПЛАСТИНКИ «ВОЯДЖЕРА»" to listOf("вояджер", "золотые пластинки"),
        "СИМПСОН" to listOf("джей симпсон", "Орентал джей"),
        "ГИБЛИ" to listOf("Миядзаки", "мой сосед тоторо", "унесённые призраками"),
        "БЕССМЕРТНЫЕ" to listOf("Французская академия"),
        "МОЙ СОСЕД ТОТОРО" to listOf("Тоторо")
    )

    fun getTags(topicName: String): List<String> {
        return topicNameToTag[topicName.toUpperCase()] ?: listOf(topicName)
    }
}