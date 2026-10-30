package cl.uc.cimuce.data.repository

import androidx.annotation.DrawableRes
import cl.uc.cimuce.R
import cl.uc.cimuce.data.model.AreaTematica.CONSTRUCCION_INDUSTRIALIZADA
import cl.uc.cimuce.data.model.AreaTematica.INGENIERIA_ESTRUCTURAL
import cl.uc.cimuce.data.model.AreaTematica.NORMALIZACION
import cl.uc.cimuce.data.model.AreaTematica.SUSTENTABILIDAD
import cl.uc.cimuce.data.model.EstadoProyecto.EN_CURSO
import cl.uc.cimuce.data.model.EstadoProyecto.FINALIZADO
import cl.uc.cimuce.data.model.EstadoProyecto.PUBLICADO
import cl.uc.cimuce.data.model.Integrante
import cl.uc.cimuce.data.model.Novedad
import cl.uc.cimuce.data.model.PerfilUsuario
import cl.uc.cimuce.data.model.PerfilUsuario.ACADEMIA
import cl.uc.cimuce.data.model.PerfilUsuario.EMPRESA
import cl.uc.cimuce.data.model.PerfilUsuario.SECTOR_PUBLICO
import cl.uc.cimuce.data.model.Proyecto
import cl.uc.cimuce.data.model.ProyectoIntegrante
import cl.uc.cimuce.data.model.TipoContenido
import cl.uc.cimuce.data.model.TipoContenido.CAPACIDAD_ID
import cl.uc.cimuce.data.model.TipoContenido.PROYECTO
import cl.uc.cimuce.data.model.TipoContenido.PUBLICACION
import java.text.Normalizer

/** Repositorio en memoria con datos ficticios (Req NF3: sin backend ni internet). */
object ProyectoRepository {

    private val imagenes = listOf(
        R.drawable.img_madera_1,
        R.drawable.img_madera_2,
        R.drawable.img_madera_3,
        R.drawable.img_madera_4,
        R.drawable.img_madera_5,
        R.drawable.img_madera_6
    )

    /** Asigna las imágenes rotando según el id (1 → img_madera_1, 7 → img_madera_1, ...). */
    @DrawableRes
    private fun imagenPara(id: Int): Int = imagenes[(id - 1) % imagenes.size]

    private fun item(
        id: Int,
        tipo: TipoContenido,
        titulo: String,
        area: String,
        ano: Int,
        estado: String,
        breve: String,
        detallada: String,
        perfiles: List<PerfilUsuario>
    ) = Proyecto(
        idProyecto = id,
        titulo = titulo,
        descripcionBreve = breve,
        descripcionDetallada = detallada,
        tipoContenido = tipo,
        areaTematica = area,
        anoEjecucion = ano,
        estadoProyecto = estado,
        urlImagen = "img_madera_${(id - 1) % imagenes.size + 1}",
        imagenRes = imagenPara(id),
        perfilesAfines = perfiles
    )

    private val items = listOf(
        // ---------- Proyectos (12) ----------
        item(
            1, PROYECTO, "Sistema de Uniones Mecánicas para CLT en Zonas Sísmicas",
            INGENIERIA_ESTRUCTURAL, 2024, EN_CURSO,
            "Uniones mecánicas para paneles CLT sometidos a cargas sísmicas.",
            "Investigación aplicada sobre el comportamiento de uniones mecánicas en paneles de madera " +
                "contralaminada (CLT) bajo cargas sísmicas. Se desarrollaron protocolos de ensayo y " +
                "especificaciones técnicas para edificios de altura media en Chile. El trabajo considera " +
                "normativa nacional e internacional para asegurar su aplicabilidad.",
            listOf(EMPRESA, ACADEMIA)
        ),
        item(
            2, PROYECTO, "Ciclo de Vida de Edificios en Madera: Huella de Carbono",
            SUSTENTABILIDAD, 2023, FINALIZADO,
            "Medición del carbono incorporado en edificios de madera.",
            "Se aplicó la metodología de análisis de ciclo de vida a cinco edificios de madera construidos " +
                "en el centro-sur de Chile. El estudio cuantificó el carbono almacenado y las emisiones de " +
                "fabricación, transporte y montaje. Los resultados se compararon con soluciones equivalentes " +
                "en hormigón armado. Como producto se entregó una base de datos abierta para equipos de diseño.",
            listOf(SECTOR_PUBLICO, ACADEMIA)
        ),
        item(
            3, PROYECTO, "Prefabricación Digital para Vivienda Social en Madera",
            CONSTRUCCION_INDUSTRIALIZADA, 2024, EN_CURSO,
            "Flujo digital de diseño y fabricación para vivienda social.",
            "El proyecto integra modelación BIM con fabricación por control numérico para producir " +
                "viviendas sociales de madera en planta. Busca reducir los plazos de obra y las pérdidas de " +
                "material respecto de la construcción tradicional. Se trabaja junto a entidades patrocinantes " +
                "para validar el sistema en un conjunto piloto.",
            listOf(SECTOR_PUBLICO, EMPRESA)
        ),
        item(
            4, PROYECTO, "Edificio Experimental de Seis Pisos en Madera",
            INGENIERIA_ESTRUCTURAL, 2025, EN_CURSO,
            "Prototipo instrumentado de edificio residencial en madera.",
            "Se diseña y construye un edificio experimental de seis pisos con muros y losas de madera " +
                "contralaminada. La estructura será instrumentada con sensores de vibración y humedad durante " +
                "sus primeros años de uso. Los datos permitirán calibrar modelos de diseño sísmico para la " +
                "realidad chilena.",
            listOf(EMPRESA, SECTOR_PUBLICO)
        ),
        item(
            5, PROYECTO, "Módulos Volumétricos para Viviendas de Emergencia",
            CONSTRUCCION_INDUSTRIALIZADA, 2022, FINALIZADO,
            "Módulos transportables de madera para respuesta ante desastres.",
            "Se desarrolló un sistema de módulos volumétricos de madera que se fabrican en planta y se " +
                "transportan completos al lugar de emplazamiento. Cada módulo se instala en menos de un día " +
                "y puede ampliarse posteriormente como vivienda definitiva. El prototipo fue evaluado en " +
                "confort térmico y resistencia estructural.",
            listOf(SECTOR_PUBLICO)
        ),
        item(
            6, PROYECTO, "Paneles Aislantes a partir de Residuos Forestales",
            SUSTENTABILIDAD, 2025, EN_CURSO,
            "Aislación térmica elaborada con corteza y aserrín.",
            "La investigación explora aglomerantes de origen biológico para fabricar paneles aislantes con " +
                "residuos de aserraderos. Se caracterizan sus propiedades térmicas, acústicas y de " +
                "comportamiento al fuego. El objetivo es ofrecer una alternativa local y de baja huella a los " +
                "aislantes sintéticos.",
            listOf(EMPRESA, ACADEMIA)
        ),
        item(
            7, PROYECTO, "Guía de Diseño para Entramados Ligeros",
            NORMALIZACION, 2021, FINALIZADO,
            "Recomendaciones de diseño para viviendas de entramado ligero.",
            "Se elaboró una guía técnica con criterios de diseño estructural y constructivo para viviendas " +
                "de entramado ligero en madera. El documento reúne detalles tipo, tablas de dimensionamiento " +
                "y buenas prácticas de obra. Fue revisado por profesionales del sector público y privado " +
                "antes de su difusión.",
            listOf(SECTOR_PUBLICO, EMPRESA)
        ),
        item(
            8, PROYECTO, "Escuelas Rurales Industrializadas en Madera",
            CONSTRUCCION_INDUSTRIALIZADA, 2026, EN_CURSO,
            "Diseño tipo replicable para establecimientos rurales.",
            "El proyecto propone un diseño tipo de escuelas rurales construidas con componentes " +
                "prefabricados de madera. Considera confort térmico y acústico para distintas zonas " +
                "climáticas del país. La propuesta se desarrolla junto a comunidades educativas para " +
                "recoger sus necesidades.",
            listOf(SECTOR_PUBLICO)
        ),
        item(
            9, PROYECTO, "Economía Circular y Desmontaje de Estructuras de Madera",
            SUSTENTABILIDAD, 2024, EN_CURSO,
            "Uniones reversibles para reutilizar piezas de madera.",
            "Se diseñan uniones reversibles que permiten desmontar edificios de madera al final de su vida " +
                "útil. El proyecto genera un catálogo de componentes reutilizables y criterios para su " +
                "reclasificación. Con ello se busca disminuir los residuos de construcción y demolición.",
            listOf(ACADEMIA, EMPRESA)
        ),
        item(
            10, PROYECTO, "Gemelo Digital para Plantas de Prefabricación",
            CONSTRUCCION_INDUSTRIALIZADA, 2025, EN_CURSO,
            "Simulación digital de líneas de producción de paneles.",
            "Se construye un gemelo digital que integra datos de sensores y planificación de producción en " +
                "plantas de paneles de madera. El modelo permite anticipar cuellos de botella y optimizar el " +
                "uso de materia prima. Su validación se realiza en una planta industrial asociada.",
            listOf(EMPRESA)
        ),
        item(
            11, PROYECTO, "Monitoreo Estructural de Puentes de Madera",
            INGENIERIA_ESTRUCTURAL, 2023, FINALIZADO,
            "Seguimiento de deformaciones y humedad en pasarelas.",
            "Se instalaron sistemas de monitoreo en tres pasarelas peatonales de madera laminada. Durante " +
                "dos años se registraron deformaciones, contenido de humedad y respuesta ante el tránsito. " +
                "Los resultados permitieron proponer planes de mantención preventiva para este tipo de obras.",
            listOf(SECTOR_PUBLICO, ACADEMIA)
        ),
        item(
            12, PROYECTO, "Clasificación Visual de Pino Radiata Estructural",
            NORMALIZACION, 2022, FINALIZADO,
            "Actualización de criterios de clasificación por resistencia.",
            "El proyecto revisó los criterios de clasificación visual de pino radiata para uso estructural. " +
                "Se ensayaron más de mil piezas para relacionar sus singularidades con la resistencia real. " +
                "Las conclusiones se entregaron como insumo para la actualización de la norma correspondiente.",
            listOf(EMPRESA, SECTOR_PUBLICO)
        ),

        // ---------- Capacidades I+D (8) ----------
        item(
            13, CAPACIDAD_ID, "Capacidad Sísmica de Marcos de Madera Laminada",
            INGENIERIA_ESTRUCTURAL, 2022, FINALIZADO,
            "Evaluación experimental de marcos resistentes a momento.",
            "El centro cuenta con experiencia en ensayos cíclicos de marcos de madera laminada encolada. " +
                "Se evalúan rigidez, ductilidad y modos de falla de las conexiones. Esta capacidad apoya a " +
                "empresas y oficinas de cálculo en la validación de sistemas estructurales.",
            listOf(ACADEMIA, EMPRESA)
        ),
        item(
            14, CAPACIDAD_ID, "Innovación en Protecciones contra Incendio en Madera",
            NORMALIZACION, 2024, EN_CURSO,
            "Desarrollo y evaluación de soluciones de protección pasiva.",
            "Se desarrollan y evalúan recubrimientos y encapsulados para mejorar la resistencia al fuego de " +
                "elementos de madera. Los ensayos miden tasas de carbonización y capacidad residual. Los " +
                "resultados apoyan la especificación técnica de proyectos y la discusión normativa.",
            listOf(EMPRESA, SECTOR_PUBLICO)
        ),
        item(
            15, CAPACIDAD_ID, "Ensayos Cíclicos de Conexiones Metálicas",
            INGENIERIA_ESTRUCTURAL, 2023, EN_CURSO,
            "Laboratorio para caracterizar conectores y anclajes.",
            "Esta capacidad permite caracterizar conectores, tornillos y anclajes usados en estructuras de " +
                "madera. Se aplican protocolos de carga monotónica y cíclica según estándares internacionales. " +
                "Los informes entregan parámetros listos para el diseño estructural.",
            listOf(ACADEMIA, EMPRESA)
        ),
        item(
            16, CAPACIDAD_ID, "Análisis de Ciclo de Vida para Productos de Madera",
            SUSTENTABILIDAD, 2024, EN_CURSO,
            "Declaraciones ambientales y cálculo de huella.",
            "El equipo realiza análisis de ciclo de vida para productos y sistemas constructivos de madera. " +
                "Se apoya a empresas en la preparación de declaraciones ambientales de producto. También se " +
                "asesora a organismos públicos en criterios de compra sustentable.",
            listOf(EMPRESA, SECTOR_PUBLICO)
        ),
        item(
            17, CAPACIDAD_ID, "Diseño para Manufactura y Ensamblaje (DfMA)",
            CONSTRUCCION_INDUSTRIALIZADA, 2025, EN_CURSO,
            "Optimización de diseños para fabricación en planta.",
            "Se asesora a equipos de diseño para adaptar sus proyectos a la fabricación industrializada. " +
                "La metodología reduce la variedad de piezas y simplifica el montaje en obra. Esta capacidad " +
                "se complementa con simulaciones de producción y logística.",
            listOf(EMPRESA)
        ),
        item(
            18, CAPACIDAD_ID, "Caracterización Higrotérmica de Envolventes",
            SUSTENTABILIDAD, 2023, FINALIZADO,
            "Estudio de humedad y temperatura en muros de madera.",
            "Se evalúa el comportamiento higrotérmico de envolventes de madera mediante simulación y " +
                "mediciones en terreno. El análisis identifica riesgos de condensación y pudrición. Las " +
                "recomendaciones mejoran la durabilidad y el confort de las viviendas.",
            listOf(ACADEMIA, SECTOR_PUBLICO)
        ),
        item(
            19, CAPACIDAD_ID, "Modelación BIM para Construcción en Madera",
            CONSTRUCCION_INDUSTRIALIZADA, 2024, EN_CURSO,
            "Bibliotecas y flujos BIM especializados en madera.",
            "El centro desarrolla bibliotecas de objetos BIM para sistemas constructivos en madera. Se " +
                "definen flujos de trabajo que conectan el modelo con la fabricación digital. Esta capacidad " +
                "se ofrece mediante capacitaciones y acompañamiento a proyectos.",
            listOf(EMPRESA, ACADEMIA)
        ),
        item(
            20, CAPACIDAD_ID, "Asesoría en Evaluación de Conformidad Normativa",
            NORMALIZACION, 2022, FINALIZADO,
            "Apoyo técnico para cumplir normas de madera estructural.",
            "Se apoya a fabricantes en la verificación del cumplimiento de normas técnicas para productos de " +
                "madera estructural. El servicio incluye revisión documental, planes de ensayo y " +
                "recomendaciones de mejora. Ha permitido a varias empresas acceder a mercados más exigentes.",
            listOf(SECTOR_PUBLICO, EMPRESA)
        ),

        // ---------- Publicaciones (6) ----------
        item(
            21, PUBLICACION, "Normalización Técnica para Madera Estructural Chilena",
            NORMALIZACION, 2023, PUBLICADO,
            "Revisión del marco normativo nacional para madera estructural.",
            "La publicación analiza el estado actual de las normas chilenas para madera estructural. Se " +
                "comparan con referentes internacionales y se identifican vacíos relevantes. El documento " +
                "propone una hoja de ruta para su actualización en los próximos años.",
            listOf(SECTOR_PUBLICO, ACADEMIA)
        ),
        item(
            22, PUBLICACION, "Comportamiento Sísmico de Edificios CLT: Estado del Arte",
            INGENIERIA_ESTRUCTURAL, 2024, PUBLICADO,
            "Revisión de la literatura sobre CLT en zonas sísmicas.",
            "El artículo revisa la evidencia experimental y numérica sobre edificios de CLT en zonas " +
                "sísmicas. Se sintetizan los factores de modificación de respuesta propuestos en distintos " +
                "países. Finalmente, se discuten las necesidades de investigación para Chile.",
            listOf(ACADEMIA)
        ),
        item(
            23, PUBLICACION, "Manual de Construcción Industrializada en Madera",
            CONSTRUCCION_INDUSTRIALIZADA, 2025, PUBLICADO,
            "Guía práctica para proyectos prefabricados en madera.",
            "El manual reúne criterios para planificar, diseñar y ejecutar proyectos industrializados en " +
                "madera. Incluye ejemplos de sistemas de paneles y módulos, junto con detalles de unión. " +
                "Está dirigido a mandantes, constructoras y equipos técnicos municipales.",
            listOf(EMPRESA, SECTOR_PUBLICO)
        ),
        item(
            24, PUBLICACION, "Carbono Almacenado en el Parque Habitacional de Madera",
            SUSTENTABILIDAD, 2024, PUBLICADO,
            "Estimación del carbono retenido en viviendas de madera.",
            "El estudio estima el carbono almacenado en las viviendas de madera construidas en Chile en la " +
                "última década. Se combinan estadísticas de edificación con factores de conversión por " +
                "producto. Los resultados aportan antecedentes para políticas de mitigación climática.",
            listOf(SECTOR_PUBLICO, ACADEMIA)
        ),
        item(
            25, PUBLICACION, "Recomendaciones para la Durabilidad de Uniones Encoladas",
            NORMALIZACION, 2023, PUBLICADO,
            "Criterios de durabilidad para climas lluviosos.",
            "La publicación recopila ensayos de envejecimiento acelerado en uniones encoladas de madera. Se " +
                "proponen criterios de selección de adhesivos según la exposición climática. El documento " +
                "busca servir como base para futuras especificaciones técnicas.",
            listOf(ACADEMIA, EMPRESA)
        ),
        item(
            26, PUBLICACION, "Diseño Sísmico de Muros de Corte en Madera",
            INGENIERIA_ESTRUCTURAL, 2025, PUBLICADO,
            "Método simplificado para muros de entramado ligero.",
            "El artículo presenta un método simplificado para el diseño sísmico de muros de corte de " +
                "entramado ligero. El procedimiento se validó con ensayos a escala real realizados en el " +
                "laboratorio. Se incluyen ejemplos de aplicación para viviendas de uno y dos pisos.",
            listOf(ACADEMIA, EMPRESA)
        )
    )

    private val integrantes = listOf(
        Integrante(1, "Dra. Valentina Rocamora", "Dra. Valentina", "Investigadora Principal", "Centro UC de Innovación en Madera"),
        Integrante(2, "Dr. Andrés Iturriaga", "Dr. Andrés", "Coinvestigador", "Escuela de Ingeniería UC"),
        Integrante(3, "Dr. Ignacio Quillay", "Dr. Ignacio", "Ingeniero de Proyectos", "Centro UC de Innovación en Madera"),
        Integrante(4, "Arq. Camila Errázuriz", "Arq. Camila", "Arquitecta Investigadora", "Escuela de Arquitectura UC"),
        Integrante(5, "Dra. Javiera Pehuén", "Dra. Javiera", "Investigadora Asociada", "Laboratorio de Biomateriales"),
        Integrante(6, "Mg. Felipe Antilef", "Mg. Felipe", "Analista de Sustentabilidad", "Centro UC de Innovación en Madera"),
        Integrante(7, "Ing. Matías Lonquimay", "Ing. Matías", "Jefe de Proyectos", "Constructora Bosque Sur"),
        Integrante(8, "Mg. Sofía Maturana", "Mg. Sofía", "Asesora Normativa", "Instituto de Normalización Técnica")
    )

    /** Integrantes por ítem (2 a 4 cada uno); de aquí se genera la tabla intermedia. */
    private val equipos = mapOf(
        1 to listOf(1, 2, 3),
        2 to listOf(6, 1),
        3 to listOf(4, 3, 7),
        4 to listOf(1, 2, 4, 7),
        5 to listOf(4, 7),
        6 to listOf(5, 6),
        7 to listOf(8, 2, 4),
        8 to listOf(4, 3),
        9 to listOf(6, 2, 4),
        10 to listOf(3, 7),
        11 to listOf(2, 3),
        12 to listOf(8, 1),
        13 to listOf(2, 1, 3),
        14 to listOf(5, 8, 3),
        15 to listOf(2, 3),
        16 to listOf(6, 5),
        17 to listOf(3, 7, 4),
        18 to listOf(6, 5, 4),
        19 to listOf(4, 3),
        20 to listOf(8, 7),
        21 to listOf(8, 1, 2),
        22 to listOf(1, 2),
        23 to listOf(4, 7, 3),
        24 to listOf(6, 1),
        25 to listOf(5, 8),
        26 to listOf(2, 1, 3, 8)
    )

    private val relaciones: List<ProyectoIntegrante> =
        equipos.flatMap { (idProyecto, ids) -> ids.map { idProyecto to it } }
            .mapIndexed { i, (idProyecto, idIntegrante) ->
                ProyectoIntegrante(i + 1, idProyecto, idIntegrante)
            }

    private val novedades = listOf(
        Novedad(
            1, "CIM UC presenta resultados en Congreso Internacional de Construcción en Madera",
            "El equipo expuso avances en uniones mecánicas para CLT y en huella de carbono de edificios. " +
                "Las presentaciones generaron interés de grupos de investigación de Canadá y Nueva Zelanda. " +
                "Se acordó preparar una propuesta conjunta para el próximo año.",
            "Septiembre 2026", imagenPara(1)
        ),
        Novedad(
            2, "Inicia la construcción del edificio experimental de seis pisos",
            "La obra contará con sensores para monitorear su comportamiento durante los próximos años. " +
                "Los paneles de CLT se fabrican en planta y se montan en menos de tres semanas. " +
                "Los datos servirán para calibrar modelos de diseño sísmico.",
            "Agosto 2026", imagenPara(2)
        ),
        Novedad(
            3, "Nuevo ciclo de capacitaciones en modelación BIM para madera",
            "Abiertas las inscripciones para profesionales de oficinas técnicas y municipios. " +
                "El curso cubre bibliotecas de objetos, coordinación de especialidades y fabricación digital. " +
                "Las sesiones serán híbridas y se entregará certificado de participación.",
            "Julio 2026", imagenPara(3)
        ),
        Novedad(
            4, "Publicado el Manual de Construcción Industrializada en Madera",
            "La guía está disponible para mandantes, constructoras y equipos técnicos. " +
                "Reúne criterios de planificación, diseño y montaje de sistemas de paneles y módulos. " +
                "Puede solicitarse una copia al equipo del centro.",
            "Junio 2026", imagenPara(4)
        )
    )

    fun getTodos(): List<Proyecto> = items

    fun getPorId(id: Int): Proyecto? = items.find { it.idProyecto == id }

    fun getPorTipo(tipo: TipoContenido): List<Proyecto> = items.filter { it.tipoContenido == tipo }

    fun contarPorTipo(tipo: TipoContenido): Int = items.count { it.tipoContenido == tipo }

    /** Filtros combinables; tipo o área nulos no filtran. El texto ignora mayúsculas y tildes. */
    fun filtrar(tipo: TipoContenido?, area: String?, texto: String): List<Proyecto> {
        val consulta = normalizar(texto.trim())
        return items.filter { p ->
            (tipo == null || p.tipoContenido == tipo) &&
                (area == null || p.areaTematica == area) &&
                (consulta.isEmpty() || listOf(p.titulo, p.areaTematica, p.descripcionBreve, p.descripcionDetallada)
                    .any { normalizar(it).contains(consulta) })
        }
    }

    fun getRecientes(n: Int): List<Proyecto> =
        getPorTipo(PROYECTO).sortedByDescending { it.anoEjecucion }.take(n)

    fun getIntegrantesDe(idProyecto: Int): List<Integrante> =
        relaciones.filter { it.idProyecto == idProyecto }.mapNotNull { getIntegrante(it.idIntegrante) }

    fun getProyectosDeIntegrante(idIntegrante: Int): List<Proyecto> =
        relaciones.filter { it.idIntegrante == idIntegrante }.mapNotNull { getPorId(it.idProyecto) }

    fun getIntegrante(id: Int): Integrante? = integrantes.find { it.idIntegrante == id }

    fun getRecomendados(perfil: PerfilUsuario): List<Proyecto> = items.filter { perfil in it.perfilesAfines }

    fun getNovedades(): List<Novedad> = novedades

    private fun normalizar(s: String): String =
        Normalizer.normalize(s, Normalizer.Form.NFD)
            .replace(Regex("\\p{Mn}+"), "")
            .lowercase()
}
