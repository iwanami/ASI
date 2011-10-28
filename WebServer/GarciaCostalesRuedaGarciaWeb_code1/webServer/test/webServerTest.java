
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import org.junit.*;
import org.junit.Assert.*;

/**
 *
 * @author Numa Trezzini
 * @author Brahim Lahlou
 * @version 
 */


public class webServerTest {
    
    private int port;
    private Socket server_connetion;
    private int test_count = 0;
    private byte[] index_html;
    private byte[] not_found_html;
    private byte[] img_jpg_html;
    private byte[] img_gif_html;
    
    public webServerTest() {
        this.port = 6789;
        try{this.server_connetion = new Socket("localhost", port);}
        catch(UnknownHostException e){
            System.out.print("hote inconnu: ");
            System.out.println(e);
        }
        catch(IOException e){
            System.out.print("erreur IO: ");
            System.out.println(e);
        }
        try{
            index_html = getArrayByteFromFile(new File(""));
            not_found_html = getArrayByteFromFile(new File(""));
            img_jpg_html = getArrayByteFromFile(new File(""));
            img_gif_html = getArrayByteFromFile(new File(""));
        }
        catch(IOException e){
            System.out.print("erreur de chargement des fichiers: ");
            System.out.println(e);
        }
        
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        System.out.println("ASI - Laboratoire 1 - Serveur Web");
        System.out.println("Debut des tests du serveur web de Laura Rueda et Fernando Garcia");
        System.out.println("=============================================================================");
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        System.out.println("=============================================================================");
        System.out.println("Fin des tests du serveur web");
    }
    
    @Before
    public void setUp() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Debut du cas test numero " + ++test_count + ":");
    }
    
    @After
    public void tearDown() {
        System.out.println("------------------------------------------------------------------------------");
        System.out.println("Fin du cas test numero " + test_count + ".");
    }

    /*
     * cas test numero 1 des specifications.
     * Obectif         : Lancer le serveur sur le port choisi. Ouvrir un navigateur et introduire l’adresse index.html.	
     * Resultat attendu: La page d’accueil est envoyée au navigateur qui l’affiche.
     */
    @Test
    public void casTest1(){
        String request = "GET /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 200 ok"));
            Assert.assertTrue(response.toString().contains(index_html.toString()));
            
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 2 des specifications.
     * Obectif         : Lancer Wireshark et ouvrir la page index.html du serveur avec un navigateur.
     *                   Le port doit correspondre à celui choisi par le serveur
     * Resultat attendu: Les paquets échangés entre le client et le serveur suivent bien le protocole TCP
     * Remarques       : Ce test n'est pas teste par java/junit
     */
    @Test
    public void casTest2(){
        
    }
    
    /*
     * cas test numero 3 des specifications.
     * Obectif         : Ouvrir un navigateur et introduire l’adresse index.html. (lancement par défaut sur le port 80)
     * Resultat attendu: La page d’accueil est affichée malgré le fait que l’on n’a pas spécifié le port.
     * Remarques       : Ce test n'est pas teste par java/junit
     */
    @Test
    public void casTest3(){
    
    }
    
    /*
     * cas test numero 4 des specifications.
     * Obectif         : Lancer un Telnet puis faire une requête HTTP 0.9 de type GET sur index.html
     * Resultat attendu: Si le serveur implémente HTTP 0.9, il doit répondre avec une ligne de statut « 200 OK ».
     *                   Sinon, il doit répondre « 400 Bad Request »
     */
    @Test
    public void casTest4(){
        String request = "GET /index.html";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            boolean contains = response.toString().contains("HTTP/0.9 200 ok") || 
                               response.toString().contains("HTTP/1.0 400 Bad Request");
            Assert.assertTrue(contains);
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 5 des specifications.
     * Obectif         : Faire une requête HTTP 1.0 de type GET sur index.html.
     * Resultat attendu: La ligne de statut de la réponse indique que le protocole utilisé est HTTP 1.0 et
     *                   le code d’état affiché est « 200 OK ».
     */
    @Test
    public void casTest5(){
        String request = "GET /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 200 ok"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 6 des specifications.
     * Obectif         : Faire une requête malformée au serveur
     * Resultat attendu: La ligne de statut de la réponse doit être « 400 Bad Request »
     */
    @Test
    public void casTest6(){
        String request = "GE /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 400 Bad Request"));
        }
        catch(IOException e){}
    
    }
    
    /*
     * cas test numero 7 des specifications.
     * Obectif         : Faire une requête POST valide au serveur
     * Resultat attendu: Le serveur renvoie la réponse avec comme code 200
     */
    @Test
    public void casTest7(){
        String request = "POST /dynamicView/form HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 200 ok"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 8 des specifications.
     * Obectif         : Faire une requête POST invalide au serveur
     * Resultat attendu: Le serveur répond avec une ligne de statut « 400 Bad Request »
     */
    @Test
    public void casTest8(){
        String request = "POS /dynamicView/form HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 400 Bad Request"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 9 des specifications.
     * Obectif         : Faire une requête HEAD au serveur
     * Resultat attendu: Le serveur répond avec une ligne de statut « 501 Not Implemented »
     */
    @Test
    public void casTest9(){
        String request = "HEAD /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 501 Not Implemented"));
        }
        catch(IOException e){}
    
    }
    
    /*
     * cas test numero 10 des specifications.
     * Obectif         : Faire plusieurs requêtes en même temps au serveur
     * Resultat attendu: Le serveur doit pouvoir traiter en parallèle les requêtes
     * Remarques       : 1000 threads sont créés puis lancés pour ce test
     */
    @Test
    public void casTest10(){
        for(int i = 0; i < 1000; i++){
            Thread t = new Thread(){
                String request = "GET /index.html HTTP/1.0";
                byte[] response = new byte[1024];
                @Override
                public void run(){
                    try{
                        OutputStream os = server_connetion.getOutputStream();
                        os.write(request.getBytes());
                        InputStream is = server_connetion.getInputStream();
                        is.read(response);
                        System.out.println(response.toString());
                        Assert.assertTrue(response.toString().contains("HTTP/1.0 200 ok"));
                    }
                    catch(IOException e){}
                }
                    
            };
            t.start();
            try{t.join();}
            catch(InterruptedException e){}
        }
    }
    
    /*
     * cas test numero 11 des specifications.
     * Obectif         : Faire une requete de type HTTP 0.9
     * Resultat attendu: Le serveur doit répondre avec une réponse simple ou avec le code de statut « 400 Bad Request »
     */
    @Test
    public void casTest11(){
        String request = "GET /index.html";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            boolean contains = response.toString().contains("HTTP/0.9 200 ok") || 
                               response.toString().contains("HTTP/1.0 400 Bad Request");
            Assert.assertTrue(contains);
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 12 des specifications.
     * Obectif         : Faire une requête et analyser la ligne de statut de la réponse
     * Resultat attendu: La réponse doit correspondre à la grammaire spécifiée sous 4.2.1
     */
    @Test
    public void casTest12(){
        String request = "GET /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 200 ok"));
            Assert.assertTrue(response.toString().contains("Content-Length:"));
            Assert.assertTrue(response.toString().contains("Location:"));
            Assert.assertTrue(response.toString().contains("Content-Type:"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 13 des specifications.
     * Obectif         : Faire une requête et analyser les en-têtes de réponse
     * Resultat attendu: Les en-têtes de réponse doivent contenir les entrées « Location » et « Server »
     */
    @Test
    public void casTest13(){
        String request = "GET /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("Location:"));
            Assert.assertTrue(response.toString().contains("Server:"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 14 des specifications.
     * Obectif         : Faire une requête pour une page html
     * Resultat attendu: L’en-tête d’entité « Content-Type » doit être « text/html »
     */
    @Test
    public void casTest14(){
        String request = "GET /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("Content-Type: text/html"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 15 des specifications.
     * Obectif         : Faire une requête pour un fichier java
     * Resultat attendu: L’en-tête d’entité « Content-Type » doit être « text/plain » (valeur par défaut)
     */
    @Test
    public void casTest15(){
        String request = "GET /[fichier java] HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("Content-Type: text/plain"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 16 des specifications.
     * Obectif         : Faire une requête pour une image JPEG
     * Resultat attendu: L’en-tête d’entité « Content-Type » doit être « image/jpeg»
     */
    @Test
    public void casTest16(){
        String request = "GET /image.jpeg HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("Content-Type: image/jpeg"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 17 des specifications.
     * Obectif         : Faire une requête pour une image GIF
     * Resultat attendu: L’en-tête d’entité « Content-Type » doit être « image/gif»
     */
    @Test
    public void casTest17(){
        String request = "GET /image.gif HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("Content-Type: image/gif"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 18 des specifications.
     * Obectif         : Faire une requête pour une page html
     * Resultat attendu: L’en-tête d’entité « Content-Length» doit correspondre au nombre d’octets du fichier source
     */
    @Test
    public void casTest18(){
        String request = "GET /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("Content-Length: "+index_html.length));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 19 des specifications.
     * Obectif         : Faire une requête pour une page html
     * Resultat attendu: Le corps de la réponse doit contenir les octets du fichier demandé. Le navigateur affiche 
     *                   donc la ressource
     */
    @Test
    public void casTest19(){
        String request = "GET /index.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains(index_html.toString()));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 20 des specifications.
     * Obectif         : Lancer le navigateur et introduire une adresse URI inexistante (par ex : laboASI.html)
     * Resultat attendu: Le serveur renvoie une page d’erreur indiquant que la page n’a pas pu être trouvée. 
     *                   Le code de statut de la réponse doit être « 404 Not Found »
     */
    @Test
    public void casTest20(){
        String request = "GET /salut.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 404 Not Found"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 21 des specifications.
     * Obectif         : Lancer le navigateur et entrer comme adresse URI la racine du serveur (http://localhost:<port>)
     * Resultat attendu: Le serveur renvoie la page par défaut (dans notre cas : index.html).
     */
    @Test
    public void casTest21(){
        String request = "GET / HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 200 ok"));
            Assert.assertTrue(response.toString().contains(index_html.toString()));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 22 des specifications.
     * Obectif         : Faire une requête sur /heig-vd.html
     * Resultat attendu: La ligne de statut de la réponse indique un code d’état «301 Moved Permanently».
     *                   La réponse doit aussi contenir l’adresse de redirection dans l’en-tête «Location».
     */
    @Test
    public void casTest22(){
        String request = "GET /heig-vd.html HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 301 Moved Permanently"));
            Assert.assertTrue(response.toString().contains("Location: heig-vd.ch"));
        }
        catch(IOException e){}
    }
    
    /*
     * cas test numero 23 des specifications.
     * Obectif         : Faire une requête levant une exception interne au serveur
     * Resultat attendu: La ligne de statut de la réponse indique un code d’état « 500 Internal Server Error »
     */
    @Test
    public void casTest23(){
        String request = "GET / HTTP/1.0";
        byte[] response = new byte[1024];
        try{
            OutputStream os = this.server_connetion.getOutputStream();
            os.write(request.getBytes());
            InputStream is = this.server_connetion.getInputStream();
            is.read(response);
            System.out.println(response.toString());
            Assert.assertTrue(response.toString().contains("HTTP/1.0 500 Internal Server Error"));
        }
        catch(IOException e){}
    }
    
    
    public static byte[] getArrayByteFromFile(File f) throws IOException {
 
        final long length = f.length();
        if (length > Integer.MAX_VALUE) { // + de 2 Go
                throw new IOException("File too big");
        }

        byte[] data = new byte[(int) length];

        final FileInputStream in = new FileInputStream(f);
        try {

                int off = 0;	// Position de lecture
                int len = data.length;	// Nombre de bytes restant à lire
                int read;		// Nb de byte lu

                do {
                        read = in.read(data, off, len);
                        if (read > 0) {
                                off += read;
                                len -= read;
                        }
                } while (read >= 0 && len > 0);

        } finally {
                in.close();
        }

        return data;
    }
    
}
