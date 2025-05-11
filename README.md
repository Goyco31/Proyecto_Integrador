<div style="justify-content: center">
    <img src="documentacion/logo.jpeg" style="display: block; margin: 0 auto">
</div>
# SkillTourney

[![Repositorio](https://img.shields.io/badge/GitHub-Proyecto-blue)](https://github.com/Goyco31/Proyecto_Integrador.gito)
[![java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=openjdk&logoColor=white)]()
[![spring](https://img.shields.io/badge/Spring-6DB33F?style=flat&logo=spring&logoColor=white)]()
[![html](https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=html5&logoColor=white)]()
[![css](https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=css3&logoColor=white)]()
[![js](https://img.shields.io/badge/JavaScript-323330?style=flat&logo=javascript&logoColor=F7DF1E)]()
[![react](https://img.shields.io/badge/React-61DAFB?style=flat&logo=react&logoColor=black)]()
[![mysql](https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white)]()
[![markdown](https://img.shields.io/badge/Markdown-000000?style=flat&logo=markdown&logoColor=white)]()

## Nosotros

<p align ="justify">Somos una plataforma de videojuegos diseñada para impulsar tanto a jugadores como a marcas hacia el siguiente nivel dentro del mundo de los eSports. Ya seas un competidor en busca de reconocimiento o una compañía en busca de visibilidad auténtica, aquí encontrarás el espacio ideal para crecer, conectar y destacar en la escena competitiva.
</p>

### Mision y Vision

<p align ="justify">🎯 Nuestra misión es construir una comunidad sólida y vibrante que conecte a jugadores de todos los niveles con marcas emergentes y consolidadas. Buscamos crear un espacio donde el talento sea reconocido, las oportunidades se multipliquen y el mundo de los eSports crezca de manera sostenible y colaborativa. A través de torneos dinámicos, rankings competitivos y herramientas integradas, queremos ser el puente entre la pasión por los videojuegos y el desarrollo profesional dentro del gaming.</p>

<p align ="justify">💡Nuestra visión es convertirnos en una referencia clave dentro del ecosistema de los eSports, siendo la plataforma ideal donde jugadores amateurs y veteranos puedan desarrollarse, competir y destacar. Aspiramos a ofrecer un entorno justo, accesible y emocionante, donde las marcas encuentren el escenario perfecto para conectar con una audiencia auténtica y comprometida.</p>

## Objetivos

- Proporcionar un aplicativo web que permita a los jugadores registrarse y participar en torneos de videojuegos de manera online.
- Ofrecer herramientas que faciliten el seguimiento del rendimiento individual y grupal (clanes) mediante sistemas de ranking y estadísticas.
- Fomentar la creación de una comunidad competitiva activa donde los usuarios puedan interactuar, competir y mejorar sus habilidades.
- Brindar una experiencia de usuario intuitiva, rápida y accesible desde cualquier dispositivo con conexión a internet.

## Alcances

- La plataforma permitirá la administracion de torneos de esports de manera online.
- Los usuarios podrán registrarse, personalizar su perfil y visualizar su historial de participación.
- La plataforma integrará funciones de notificaciones por correo electrónico para alertas importantes.
- Habrá visibilidad pública de torneos en curso y finalizados.
- Se contempla la integración con plataformas de streaming para visualizar partidas en tiempo real.
- El sistema será accesible desde distintos dispositivos gracias a su diseño responsivo.

## Limitaciones

- No se incluye en esta versión la verificación automática de resultados (debe ser registrada manualmente por organizadores o administradores).
- La aplicación dependerá de servicios externos (como correos o streaming) que podrían presentar interrupciones ajenas al sistema.
- No se garantiza compatibilidad con navegadores obsoletos o sin soporte moderno.
- No se contempla la creación de una app nativa móvil en esta etapa del proyecto.
- La moderación de contenido ofensivo en perfiles o chats no está automatizada, será responsabilidad del administrador intervenir cuando se detecten abusos.
- Actualmente solo se ecuenta con dos juegos en el sistema de torneos

| ![Imagen 1](documentacion/dota.png) | ![Imagen 2](documentacion/conter.png) |
| --------------------- | ----------------------- |

## Funcionalidades

- El sistema debe permitir que los usuarios se registren he inicien sesion utilizando su correo electrónico y una contraseña segura.
- Los usuarios deben poder editar su perfil.
- El sistema debe mostrar a cada usuario su historial de torneos jugados junto con los resultados obtenidos.
- Los organizadores deben tener la posibilidad de crear y administrar sus propios torneos.
- Los jugadores deben poder inscribirse en los torneos disponibles desde la plataforma.
- El sistema debe enviar notificaciones importantes al correo electrónico de los usuarios.

## Proceso de registro a un torneo

```mermaid
flowchart TD
    A((Inicio))
        B[/¿Tienes una cuenta?/]
        C{usuario y contraseña}

        D[Seccion de torneos]
        E[\Elegir torneo\]
        F{¿Eres mayor de edad?}

        G[Fase de registro]
        H{¿Necesita un pago?}

        I[Validar metodo de pago]
        J[Registro completado]
        Z((Fin))

        D2[Creacion de cuenta]
        D3[\Usuario, correo y contraseña\]
        D4[Validacion de datos]
        D5{Datos guardados}
        DDD[/Datos ya existentes, intente nuevamente/]

        G2[Validacion de un adulto]
        G3{Confirmacion}


        A-->B-->C-->|Si|D-->E-->F-->|Si|G-->H-->|Si|I-->J-->Z
        C-->|No|D2-->D3-->D4-->D5-->|Si|D
        D5-->|No|DDD-->D2
        F-->|No|G2-->G3-->|Si|G
        G3-->|No|Z
        H-->|No|J
```

## Tecnologias usadas

- **Java**

  El lenguaje de programacion escogir para este proyecto es [Java](https://www.youtube.com/watch?v=crBLydQRUsk). Siendo un lenguaje de tipo multiplataforma y orientado a objetos, se ejecuta en miles de millones de dispositivos, incluyendo computadoras, teléfonos inteligentes, dispositivos IoT y más.

- **Spring boot**

  Java Spring Boot ([Spring Boot](https://www.youtube.com/watch?v=wlZWt_fIAyM)) es una herramienta que hace que el desarrollo de aplicaciones web y microservicios con Spring Framework sea más rápido y fácil a través de tres funcionalidades principales:

  1. Configuración automática

  2. Un enfoque obstinado de la configuración

  3. La capacidad de crear aplicaciones independientes

  Estas características funcionan juntas para brindarle una herramienta que le permite configurar una aplicación basada en Spring con una configuración y preparación mínimas.

- **Spring Security**

  [Spring Security](https://www.youtube.com/watch?v=fmKkU5uuNr0) es un marco que se centra en proporcionar autenticación y autorización a las aplicaciones Java. Al igual que todos los proyectos de Spring, el poder real de Spring Security se encuentra en la facilidad con la que se puede extender para cumplir con los requisitos personalizados

  _Características:_

  - _Soporte integral y extensible tanto para Autenticación como para Autorización_

  - _Protección contra ataques como fijación de sesión, clickjacking, falsificación de solicitudes entre sitios, etc_

  - _Integración de API de Servlet_

  - _Integración opcional con Spring Web MVC_

  - Mucho más...

- **HTML, CSS y JavaScript**

  [HTML](https://www.youtube.com/watch?v=10GHKjgQIR0), [CSS](https://www.youtube.com/watch?v=8cSo0ijtkzU) y [Javascript](https://www.youtube.com/watch?v=riZbwRFMFuw) son tres lenguajes que se utilizan en el Front End, es decir, que se utilizan en el lado del usuario. HTML existe desde los inicios de Internet y a partir del crecimiento de este lenguaje surge CSS para mejorar la parte estética de los website creados a partir de HTML. Por último surge Javascript para darle interactividad.

  - HTML: 
  
    Se utiliza para programar la estructura semántica de un website a través del uso de etiquetas.

  - CSS: 
  
    Se trata de un lenguaje de estilo, por ende, se usa para programar la estética y visual del sitio.

  - JavaScript: 
  
    Su función es proporcionar dinamismo e interactividad a los websites, pero también posibilita el procesamiento y transformación de los datos enviados y recibidos.
  

- MySQL

    [MySQL](https://www.youtube.com/watch?v=PXuMYHvvFrA) es el sistema de gestión de bases de datos de código abierto más popular del mundo. Las bases de datos son los repositorios de información esencial para todas las aplicaciones de software. Por ejemplo, cada vez que alguien realiza una búsqueda en Internet, inicia sesión en una cuenta o completa una transacción, una base de datos almacena la información para poder acceder a ella en el futuro. MySQL sobresale en esta tarea.

## Base de datos MySQL ER (Entidad-Relacion)

```mermaid
    erDiagram

    Usuario {
        int id_usuario PK
        varchar nombre
        varchar apellido
        varchar nickname
        varchar correo
        varchar contraseña
        datetime fecha_registro
    }

    InfoUsuario {
        int id_usuario FK
        mediumblob img_perfil
        mediumblob img_banner
        int id_inscripcion FK
        varchar discord
        varchar youtube
        varchar twitch
        varchar kick
    }

    Rol {
        int id_usuario FK
        varchar tipo_usuario
    }

    Monedas {
        int id_usuario FK
        decimal saldo
    }

    Recarga {
        int id_recarga PK
        int id_usuario FK
        datetime fecha_recarga
        int cantidad
        decimal monto_pagar
        enum tipo_pago
        enum estado
    }

    Mensajes {
        int id_mensajes PK
        int id_emisor_user FK
        int id_receptor_user FK
        text contenido
        datetime fecha_envio
        tinyint leido
    }

    Juego {
        int id_juego PK
        varchar nombre_juego
        varchar genero_juego
        mediumblob img
    }

    Torneo {
        int id_torneo PK
        int id_juego FK
        varchar nombre_torneo
        text decripcion
        enum tipo
        decimal precio_inscripcion
        decimal premio
        varchar modo_juego
        datetime fecha_inicio
        datetime fecha_fin
        enum estado
    }

    Equipo {
        int id_equipo PK
        varchar nombre_equipo
        datetime fecha_creacion
    }

    MiembrosEquipo {
        int id_usuario FK
        int id_equipo FK
    }

    Inscripciones {
        int id_inscripciones PK
        int id_equipo FK
        int id_torneo FK
        datetime fecha_inscripcion
    }

    Partida {
        int id_partida PK
        int id_torneo FK
        int id_equipo1 FK
        int id_equipo2 FK
        datetime fecha_partida
        enum estado
    }

    Ronda {
        int id_ronda PK
        int id_partida FK
        int numero_ronda
        int id_equipo_ganador FK
        int puntaje_equipo1
        int puntaje_equipo2
        int id_mvp FK
        int total_kills_equipo1
        int total_kills_equipo2
    }

    EstadisticaRonda {
        int id_estadistica_ronda PK
        int id_ronda FK
        int id_usuario FK
        int kills
        int asistencias
        int muertes
        varchar rol
        tinyint es_mvp
    }

    Usuario ||--|| InfoUsuario : tiene
    Rol||--||Usuario: tiene
    Monedas||--||Usuario: tiene
    Recarga||--|{Usuario: realiza
    Mensajes}o--||Usuario: "envia/recibe"

    Juego ||--|{ Torneo: tiene
    MiembrosEquipo}|--||Equipo: tiene
    Usuario||--|{MiembrosEquipo: es
    Torneo||--|{Inscripciones: tiene
    Inscripciones}|--||Equipo: tiene

    Torneo||--|{Partida: tiene
    Equipo}|--||Partida: tiene
    Partida||--|{Ronda: tiene
    EstadisticaRonda||--||Ronda: tiene
```

## Contacto

### Enlaces de contacto

[![correo](https://img.shields.io/badge/Gmail-EA4335?style=flat&logo=gmail&logoColor=white)](skilltourney@gmail.com)
[![discord](https://img.shields.io/badge/Discord-5865F2?style=flat&logo=discord&logoColor=white)]()
[![instagram](https://img.shields.io/badge/Instagram-E4405F?style=flat&logo=instagram&logoColor=white)]()
[![twitch](https://img.shields.io/badge/Twitch-9146FF?style=flat&logo=twitch&logoColor=white)]()
[![tiktok](https://img.shields.io/badge/TikTok-000000?style=flat&logo=tiktok&logoColor=white)]()
[![twitter](https://img.shields.io/badge/Twitter-000000?style=flat&logo=x&logoColor=white)]()

---
***Proeycto en proceso***
