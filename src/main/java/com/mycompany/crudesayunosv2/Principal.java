package com.mycompany.crudesayunosv2;
/**/
import java.util.Scanner;
import models.Pedidos;
import models.Productos;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
/**
 ** @author Ridouan Abdellah Tieb 
 **/
public class Principal {

    public static void main(String[] args) {
        
        /*
        Creo la conversion de la fecha de java.sql.Date a java.util.Date
         */
        java.util.Date utilDate = new java.util.Date();
        long lnMilisegundos = utilDate.getTime();
        java.sql.Date sqlDate = new java.sql.Date(lnMilisegundos);
        
        /*
        Abro la sesión.
        */
        Session s = HibernateUtil.getSessionFactory().openSession();
    
        /*
        Creo un objeto de la clase Scanner para introducir datos por teclado. 
        Creo el menú del programa con un while y un switch donde ocurriran los 
        eventos. 
         */        
        Scanner sc = new Scanner(System.in);
        int opcion = 0;
        /*
        Creo el menú del programa Comandas 2.0
        */
        System.out.println("\n\nBIENVENIDO A COMANDAS 2.0");

        System.out.println("\n¿QUE DESEA HACER?");

        while (opcion != 5) {

            System.out.println("\n1. CREAR UN NUEVO PEDIDO");
            System.out.println("\n2. ELIMINAR UN PEDIDO EXISTENTE");
            System.out.println("\n3. MARCAR UN PEDIDO COMO RECOGIDO");
            System.out.println("\n4. LISTAR LAS COMANDAS PENDIENTES DE HOY");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    /*
                    Listo la lista de productos
                    */
                    Query q = s.createQuery("FROM Productos");
                    q.list().forEach( (e)-> System.out.println("\n"+e));
                    
                    System.out.println("\nLA DISPONIBILIDAD DEL PRODUCTO VIENE "
                     + "REFLEJADA DE LA SIGUIENTE MANERA: 0 = NO DISPONIBLE Y "
                     + "1 = DISPONIBLE");
                    /*
                    Solicito los datos necesarios para hacer un nuevo pedido
                    */
                    System.out.println("\nINTRODUZCA EL NOMBRE DEL ALUMNO");
                    String nombre = sc.next();
                    
                    System.out.println("\nINTRODUZCA EL MÓDULO DEL ALUMNO");
                    String modulo = sc.next();
                    
                    System.out.println("\nINTRODUZCA EL ID DEL PRODUCTO");
                    int producto= sc.nextInt();
                    /*
                    Creo el objeto e introduzco los parámetros para crearlo.
                    */
                    Pedidos p = new Pedidos();                           
                    p.setUsuario(nombre);
                    p.setCiclo(modulo);
                    p.setFecha(sqlDate);
                    p.setEstado(0);
                    p.setProductos((Productos) q.list().get(producto-1));
                    /*
                    Hago la transacción, guardo y lo subo. 
                    */
                    Transaction tr = s.beginTransaction();
                    s.save(p);
                    tr.commit();
                    
                    break;
                case 2:
                    /*
                    Listo la lista de productos.
                    */
                    Query qq = s.createQuery("FROM Pedidos");
                    qq.list().forEach((e)-> System.out.println("\n"+e));
                    
                    System.out.println("\nEL ESTADO DEL PEDIDO VIENE REFLEJADO DE "
                                   + "LA SIGUIENTE MANERA: 0 = PENDIENTE Y "
                                   + "1 = RECOGIDO\n");
                    /*
                    Solicito los datos necesarios para eliminar el pedido. 
                    */
                    System.out.println("INDIQUE EL ID DEL PEDIDO A ELIMINAR");
                    int elimino = sc.nextInt();
                    /*
                    Creo la transacción de información.
                    */
                    Transaction ta = s.beginTransaction();   
                    /*
                    Creo la consulta que eliminará el pedido
                    */
                    Query qa = s.createQuery("DELETE FROM Pedidos WHERE id =:id");
                    /*
                    Le paso los parámetros que necesita para eliminar el pedido.
                    */    
                    qa.setParameter("id", elimino);
                    /*
                    Ejecuto la consulta dentro de una variable que luego 
                    imprimire junto a un mensaje para asegurarme que se 
                    ejecuta la query. 
                    */
                    int cont = qa.executeUpdate();
                    System.out.println(cont + " PEDIDO BORRADO");
                    /*
                    Finalmente subo el proceso. 
                    */
                    ta.commit();

                    break;

                case 3:
                    /*
                    Listo la lista de pedidos.
                    */
                    Query qb = s.createQuery("FROM Pedidos");
                    qb.list().forEach((e)-> System.out.println("\n"+e));
                    
                    System.out.println("EL ESTADO DEL PEDIDO VIENE REFLEJADO DE "
                                        + "LA SIGUIENTE MANERA: 0 = PENDIENTE Y "
                                        + "1 = RECOGIDO\n\n");
                    /*
                    Solicito los datos del pedido necesarios para marcar un 
                    pedido como recogido.
                    */
                    System.out.println("INDIQUE EL ID DEL PEDIDO QUE QUIERE "
                                        + "MARCAR COMO RECOGIDO");                   
                    int recojo = sc.nextInt();
                    /*
                    Creo la transacción de información.
                    */
                    Transaction tb = s.beginTransaction();
                    /*
                    Creo la query que me permitira cambiar el estado del pedido.
                    */
                    Query qc = s.createQuery("UPDATE Pedidos SET estado =:estado WHERE id =:id");                   
                    qc.setParameter("id", recojo);
                    qc.setParameter("estado", 1);
                    /*
                    Ejecuto la consulta dentro de una variable que luego 
                    imprimire junto a un mensaje para asegurarme que se 
                    ejecuta la query.  
                    */
                    int cunt = qc.executeUpdate();
                    System.out.println(cunt + " PEDIDO MARCADO COMO RECOGIDO");
                    /*
                    Finalmente subo el proceso. 
                    */
                    tb.commit();
                    
                    break;
                case 4:
                    /*
                    Creo la query que me permitira listar los pedidos pendientes
                    de hoy.
                    */
                    Query qd = s.createQuery("FROM Pedidos WHERE fecha =:fecha AND estado =:estado ");
                    /*
                    Introduzco los parámetros requeridos.
                    */
                    qd.setParameter("fecha", sqlDate);
                    qd.setParameter("estado", 0);
                    /*
                    Recorro la lista y la muestro por consola.
                    */
                    qd.list().forEach((e)-> System.out.println("\n"+e));
                    
                    break;
            }
        }
    }
}
 
    

    
    

