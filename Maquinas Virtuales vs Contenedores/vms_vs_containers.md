# Máquinas virtuales vs contenedores

Las máquinas virtuales y los contenedores son tecnologías usadas para ejecutar aplicaciones en entornos aislados. Ambas permiten separar una aplicación o sistema del resto del equipo, pero funcionan de manera diferente. La principal diferencia está en el nivel donde ocurre la virtualización: una máquina virtual virtualiza hardware completo, mientras que un contenedor virtualiza el entorno de ejecución sobre el sistema operativo.

## ¿Qué es una máquina virtual?

Una máquina virtual, o VM, es un entorno de cómputo que simula una computadora completa. Dentro de una VM se puede instalar un sistema operativo completo, llamado sistema operativo invitado. Ese sistema invitado cree que está usando una computadora física, pero en realidad está usando recursos virtualizados del equipo anfitrión.

Para que esto funcione, se utiliza un **hipervisor**. El hipervisor es una capa de software que administra los recursos físicos, como CPU, memoria, almacenamiento y red, y los reparte entre una o varias máquinas virtuales.

### Arquitectura general

Arquitectura típica de una máquina virtual:

- Hardware físico del equipo anfitrión
- Sistema operativo anfitrión o hipervisor directo sobre el hardware
- Hipervisor encargado de crear y administrar las máquinas virtuales
- Sistema operativo invitado dentro de cada máquina virtual
- Aplicaciones ejecutándose dentro del sistema operativo invitado

Cada máquina virtual tiene su propio sistema operativo completo. Por ejemplo, una computadora con macOS, Windows o Linux puede ejecutar una máquina virtual con Ubuntu, Windows Server u otro sistema operativo.

### Uso de recursos

Las máquinas virtuales suelen consumir más recursos que los contenedores porque cada VM necesita su propio sistema operativo. Esto implica reservar memoria RAM, espacio en disco y capacidad de procesamiento para cada sistema invitado.

Aunque ofrecen un aislamiento fuerte, también requieren más almacenamiento y más tiempo de procesamiento. Si se ejecutan muchas máquinas virtuales en un mismo equipo, el consumo de recursos puede crecer rápidamente.

### Tiempo de arranque

El tiempo de arranque de una máquina virtual suele ser mayor porque debe iniciar un sistema operativo completo. El proceso es parecido a encender una computadora física: se carga el sistema, servicios, controladores y finalmente las aplicaciones.

Por esta razón, una VM puede tardar desde varios segundos hasta algunos minutos en estar lista, dependiendo del sistema operativo, el hardware y la configuración.

### Relación con el sistema operativo anfitrión

Una máquina virtual está más separada del sistema operativo anfitrión. Aunque usa sus recursos físicos, no comparte directamente el núcleo del sistema operativo anfitrión. Cada VM tiene su propio kernel o núcleo del sistema operativo invitado.

Esto permite ejecutar sistemas operativos diferentes al del anfitrión. Por ejemplo, se puede ejecutar Linux dentro de una máquina virtual en una computadora con Windows.

## ¿Qué es un contenedor?

Un contenedor es una unidad ligera que empaqueta una aplicación junto con sus dependencias, bibliotecas y configuración necesaria para ejecutarse. A diferencia de una máquina virtual, un contenedor no incluye un sistema operativo completo propio. En cambio, comparte el kernel del sistema operativo anfitrión.

Los contenedores son comunes en herramientas como Docker, Podman y Kubernetes. Se usan mucho en desarrollo web, despliegue de aplicaciones, pruebas y ambientes de producción.

### Arquitectura general

La arquitectura típica de un contenedor incluye:

- Hardware físico del equipo anfitrión.
- Sistema operativo anfitrión.
- Motor de contenedores, como Docker Engine.
- Contenedores ejecutándose como procesos aislados.
- Aplicaciones y dependencias dentro de cada contenedor.

Cada contenedor tiene su propio espacio aislado para archivos, procesos y configuración, pero comparte el kernel del sistema operativo anfitrión con otros contenedores.

### Uso de recursos

Los contenedores consumen menos recursos que las máquinas virtuales porque no necesitan ejecutar un sistema operativo completo por cada aplicación. Como comparten el kernel del anfitrión, son más livianos y permiten ejecutar más aplicaciones en el mismo equipo.

También suelen ocupar menos espacio en disco. Una imagen de contenedor puede pesar desde pocos megabytes hasta varios cientos, dependiendo de la aplicación y sus dependencias. En cambio, una máquina virtual puede necesitar varios gigabytes porque incluye un sistema operativo completo.

### Tiempo de arranque

El tiempo de arranque de un contenedor es mucho menor que el de una máquina virtual. Como no necesita iniciar un sistema operativo completo, normalmente solo debe iniciar el proceso principal de la aplicación.

Por eso, un contenedor puede arrancar en segundos o incluso menos, lo que lo hace útil para despliegues rápidos, escalamiento automático y ambientes donde las aplicaciones se crean y destruyen constantemente.

### Relación con el sistema operativo anfitrión

Un contenedor depende más directamente del sistema operativo anfitrión porque comparte su kernel. Esto significa que los contenedores Linux normalmente necesitan un kernel Linux, y los contenedores Windows necesitan un entorno compatible con Windows.

Aunque los contenedores están aislados, no tienen el mismo nivel de separación que una máquina virtual completa. Su aislamiento se basa en mecanismos del sistema operativo, como espacios de nombres, control de recursos y permisos.

## Comparación general

| Aspecto | Máquina virtual | Contenedor |
|---|---|---|
| Nivel de virtualización | Virtualiza hardware completo | Virtualiza el entorno de ejecución del sistema operativo |
| Sistema operativo | Cada VM tiene su propio sistema operativo invitado | Comparte el kernel del sistema operativo anfitrión |
| Uso de recursos | Mayor consumo de RAM, CPU y disco | Menor consumo de recursos |
| Tiempo de arranque | Más lento, porque inicia un sistema operativo completo | Más rápido, porque inicia solo la aplicación o proceso |
| Aislamiento | Más fuerte, porque separa sistemas operativos completos | Ligero, basado en aislamiento del sistema operativo |
| Portabilidad | Permite ejecutar sistemas operativos distintos al anfitrión | Muy portable para aplicaciones, pero depende del kernel compatible |
| Tamaño típico | Puede ocupar varios GB | Puede ocupar MB o menos GB |

## Docker vs VMs
![Docker vs VMs](docker_vs_vm.png)
