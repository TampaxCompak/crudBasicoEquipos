package miPaquetillo;

import jakarta.persistence.*;
import java.util.List;
import java.util.Scanner;

public class appPrueba {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("unidad-equipo");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        try {
            // Insertar datos iniciales, comentado cuando ya lo hemos hecho
            //insertarDatosIniciales(em);

            while (true) {
                System.out.println("\n--- CRUD Equipos y Jugadores ---");
                System.out.println("1. Crear Equipo");
                System.out.println("2. Leer Equipos");
                System.out.println("3. Actualizar Equipo");
                System.out.println("4. Eliminar Equipo");
                System.out.println("5. Crear Jugador");
                System.out.println("6. Leer Jugadores");
                System.out.println("7. Actualizar Jugador");
                System.out.println("8. Eliminar Jugador");
                System.out.println("9. Salir");
                System.out.print("Seleccione una opción: ");
                int opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar buffer

                switch (opcion) {
                    case 1 -> crearEquipo(em, scanner);
                    case 2 -> leerEquipos(em);
                    case 3 -> actualizarEquipo(em, scanner);
                    case 4 -> eliminarEquipo(em, scanner);
                    case 5 -> crearJugador(em, scanner);
                    case 6 -> leerJugadores(em);
                    case 7 -> actualizarJugador(em, scanner);
                    case 8 -> eliminarJugador(em, scanner);
                    case 9 -> {
                        System.out.println("Saliendo...");
                        return;
                    }
                    default -> System.out.println("Opción no válida.");
                }
            }
        } finally {
            em.close();
            emf.close();
            scanner.close();
        }
    }
    /*
    private static void insertarDatosIniciales(EntityManager em) {
        em.getTransaction().begin();

        for (int i = 1; i <= 3; i++) {
            Equipo equipo = new Equipo();
            equipo.setNombre("Equipo " + i);
            equipo.setEstadio("Estadio " + i);
            em.persist(equipo);

            for (int j = 1; j <= 5; j++) {
                Jugador jugador = new Jugador();
                jugador.setNombre("Jugador " + j + " del Equipo " + i);
                jugador.setEstatura(1.70f + (j * 0.01f));
                jugador.setPeso((float) (70 + j));
                jugador.setIdEquipo(equipo);
                em.persist(jugador);
            }
        }

        em.getTransaction().commit();

        System.out.println("Datos iniciales insertados: 3 equipos y 5 jugadores por equipo.");
    }

     */
    // Métodos CRUD para Equipo
    private static void crearEquipo(EntityManager em, Scanner scanner) {
        System.out.print("Ingrese el nombre del equipo: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el nombre del estadio: ");
        String estadio = scanner.nextLine();

        Equipo equipo = new Equipo();
        equipo.setNombre(nombre);
        equipo.setEstadio(estadio);

        em.getTransaction().begin();
        em.persist(equipo);
        em.getTransaction().commit();

        System.out.println("Equipo creado con éxito.");
    }

    private static void leerEquipos(EntityManager em) {
        List<Equipo> equipos = em.createQuery("FROM Equipo", Equipo.class).getResultList();
        System.out.println("Lista de Equipos:");
        for (Equipo equipo : equipos) {
            System.out.println("ID: " + equipo.getId() + ", Nombre: " + equipo.getNombre() + ", Estadio: " + equipo.getEstadio());
        }
    }

    private static void actualizarEquipo(EntityManager em, Scanner scanner) {
        System.out.print("Ingrese el ID del equipo a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        Equipo equipo = em.find(Equipo.class, id);
        if (equipo == null) {
            System.out.println("Equipo no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del equipo: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Nuevo estadio del equipo: ");
        String nuevoEstadio = scanner.nextLine();

        em.getTransaction().begin();
        equipo.setNombre(nuevoNombre);
        equipo.setEstadio(nuevoEstadio);
        em.getTransaction().commit();

        System.out.println("Equipo actualizado con éxito.");
    }

    private static void eliminarEquipo(EntityManager em, Scanner scanner) {
        System.out.print("Ingrese el ID del equipo a eliminar: ");
        int id = scanner.nextInt();

        Equipo equipo = em.find(Equipo.class, id);
        if (equipo == null) {
            System.out.println("Equipo no encontrado.");
            return;
        }

        em.getTransaction().begin();
        em.remove(equipo);
        em.getTransaction().commit();

        System.out.println("Equipo eliminado con éxito.");
    }

    // Métodos CRUD para Jugador
    private static void crearJugador(EntityManager em, Scanner scanner) {
        System.out.print("Ingrese el nombre del jugador: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese la estatura del jugador: ");
        float estatura = scanner.nextFloat();
        System.out.print("Ingrese el peso del jugador: ");
        float peso = scanner.nextFloat();
        System.out.print("Ingrese el ID del equipo al que pertenece: ");
        int idEquipo = scanner.nextInt();

        Equipo equipo = em.find(Equipo.class, idEquipo);
        if (equipo == null) {
            System.out.println("Equipo no encontrado.");
            return;
        }

        Jugador jugador = new Jugador();
        jugador.setNombre(nombre);
        jugador.setEstatura(estatura);
        jugador.setPeso(peso);
        jugador.setIdEquipo(equipo);

        em.getTransaction().begin();
        em.persist(jugador);
        em.getTransaction().commit();

        System.out.println("Jugador creado con éxito.");
    }

    private static void leerJugadores(EntityManager em) {
        List<Jugador> jugadores = em.createQuery("FROM Jugador", Jugador.class).getResultList();
        System.out.println("Lista de Jugadores:");
        for (Jugador jugador : jugadores) {
            System.out.println("ID: " + jugador.getId() + ", Nombre: " + jugador.getNombre() + ", Estatura: " + jugador.getEstatura() + ", Peso: " + jugador.getPeso() + ", Equipo: " + jugador.getIdEquipo().getNombre());
        }
    }

    private static void actualizarJugador(EntityManager em, Scanner scanner) {
        System.out.print("Ingrese el ID del jugador a actualizar: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        Jugador jugador = em.find(Jugador.class, id);
        if (jugador == null) {
            System.out.println("Jugador no encontrado.");
            return;
        }

        System.out.print("Nuevo nombre del jugador: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Nueva estatura del jugador: ");
        float nuevaEstatura = scanner.nextFloat();
        System.out.print("Nuevo peso del jugador: ");
        float nuevoPeso = scanner.nextFloat();

        em.getTransaction().begin();
        jugador.setNombre(nuevoNombre);
        jugador.setEstatura(nuevaEstatura);
        jugador.setPeso(nuevoPeso);
        em.getTransaction().commit();

        System.out.println("Jugador actualizado con éxito.");
    }

    private static void eliminarJugador(EntityManager em, Scanner scanner) {
        System.out.print("Ingrese el ID del jugador a eliminar: ");
        int id = scanner.nextInt();

        Jugador jugador = em.find(Jugador.class, id);
        if (jugador == null) {
            System.out.println("Jugador no encontrado.");
            return;
        }

        em.getTransaction().begin();
        em.remove(jugador);
        em.getTransaction().commit();

        System.out.println("Jugador eliminado con éxito.");
    }
}