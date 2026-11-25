package Proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

/**
 * Fase 2: interiores transitables de CASA, TIENDA y CENTRO_POKEMON.
 * Todas las texturas y todos los muebles proceden del mismo atlas
 * proporcionado por el usuario. No crea sprites alternativos.
 * El indicador de jugador es únicamente una marca del control de UI.
 */
public final class InterioresAtlasUsuario {
    public static final int TILE=16;
    public static final int ESCALA=3;
    public static final int ANCHO=18;
    public static final int ALTO=13;

    public enum Tipo { CASA, TIENDA, CENTRO_POKEMON }

    public record Mueble(int x,int y,String sprite,String descripcion) { }

    public static final class Interior {
        private final Tipo tipo;
        private final String[][] suelos = new String[ALTO][ANCHO];
        private final String[][] paredes = new String[ALTO][ANCHO];
        private final boolean[][] bloqueado = new boolean[ALTO][ANCHO];
        private final List<Mueble> muebles=new ArrayList<>();
        private final int puertaX=ANCHO/2, puertaY=ALTO-1;
        private final AtlasUsuario16 atlas;

        private Interior(Tipo tipo,AtlasUsuario16 atlas){
            this.tipo=tipo;this.atlas=atlas;
        }
        public Tipo tipo(){return tipo;}
        public String suelo(int x,int y){return suelos[y][x];}
        public String pared(int x,int y){return paredes[y][x];}
        public List<Mueble> muebles(){return List.copyOf(muebles);}
        public int puertaX(){return puertaX;}
        public int puertaY(){return puertaY;}
        public boolean puedePisar(int x,int y){
            return x>=0&&y>=0&&x<ANCHO&&y<ALTO&&!bloqueado[y][x];
        }
        public boolean esSalida(int x,int y){return x==puertaX&&y==puertaY;}
        /** Mueble vecino al jugador. Devuelve la entidad real, no solo su texto. */
        public Mueble muebleCercano(int x,int y){
            for(Mueble m:muebles){
                BufferedImage im=atlas.spriteInterior(m.sprite());
                int ax=Math.max(1,(im.getWidth()+TILE-1)/TILE);
                int ay=Math.max(1,(im.getHeight()+TILE-1)/TILE);
                for(int yy=m.y();yy<m.y()+ay;yy++)
                    for(int xx=m.x();xx<m.x()+ax;xx++)
                        if(Math.abs(x-xx)+Math.abs(y-yy)==1)
                            return m;
            }
            return null;
        }
        public String interactuar(int x,int y){
            Mueble encontrado = muebleCercano(x,y);
            return encontrado == null ? null : encontrado.descripcion();
        }
        private void mueble(int x,int y,String nombre,String descripcion){
            BufferedImage sprite=atlas.spriteInterior(nombre);
            int ancho=(sprite.getWidth()+TILE-1)/TILE;
            int alto=(sprite.getHeight()+TILE-1)/TILE;
            if(x<1||y<2||x+ancho>=ANCHO-1||y+alto>=ALTO-1)
                throw new IllegalArgumentException("Mueble fuera del interior: "+nombre);
            for(int yy=y;yy<y+alto;yy++)
                for(int xx=x;xx<x+ancho;xx++){
                    if(bloqueado[yy][xx])
                        throw new IllegalArgumentException("Mueble superpuesto: "+nombre);
                    bloqueado[yy][xx]=true;
                }
            muebles.add(new Mueble(x,y,nombre,descripcion));
        }
        /** Comprobar que cada habitación cuenta con una salida accesible. */
        public boolean salidaAlcanzable() {
            boolean[][] visto=new boolean[ALTO][ANCHO];
            ArrayDeque<Point> cola=new ArrayDeque<>();
            cola.add(new Point(puertaX,ALTO-2));
            while(!cola.isEmpty()){
                Point p=cola.removeFirst();
                if(!puedePisar(p.x,p.y)||visto[p.y][p.x])continue;
                if(esSalida(p.x,p.y))return true;
                visto[p.y][p.x]=true;
                cola.add(new Point(p.x+1,p.y));
                cola.add(new Point(p.x-1,p.y));
                cola.add(new Point(p.x,p.y+1));
                cola.add(new Point(p.x,p.y-1));
            }
            return false;
        }
        public boolean muebleAccesible(String nombre){
            for(Mueble m:muebles){
                if(!m.sprite().equals(nombre))continue;
                int ancho=(atlas.spriteInterior(nombre).getWidth()+TILE-1)/TILE;
                int alto=(atlas.spriteInterior(nombre).getHeight()+TILE-1)/TILE;
                for(int y=m.y();y<m.y()+alto;y++)
                    for(int x=m.x();x<m.x()+ancho;x++)
                        for(int[] d:new int[][]{{0,1},{1,0},{0,-1},{-1,0}})
                            if(puedePisar(x+d[0],y+d[1]))return true;
            }
            return false;
        }
    }

    private InterioresAtlasUsuario(){}

    public static Interior generar(Tipo tipo,AtlasUsuario16 atlas){
        if(tipo==null||atlas==null)throw new IllegalArgumentException("Interior y atlas requeridos");
        Interior i=new Interior(tipo,atlas);
        String piso=switch(tipo){
            case CASA -> "PISO_CASA";
            case TIENDA -> "PISO_TIENDA";
            case CENTRO_POKEMON -> "PISO_CENTRO";
        };
        String pared=switch(tipo){
            case CASA -> "PARED_CASA";
            case TIENDA -> "PARED_TIENDA";
            case CENTRO_POKEMON -> "PARED_CENTRO";
        };
        for(int y=0;y<ALTO;y++)for(int x=0;x<ANCHO;x++){
            i.suelos[y][x]=piso;
            if(x==0||x==ANCHO-1||y<2||y==ALTO-1){
                i.paredes[y][x]=pared;
                i.bloqueado[y][x]=true;
            }
        }
        i.bloqueado[ALTO-1][ANCHO/2]=false;
        i.paredes[ALTO-1][ANCHO/2]=null;

        switch(tipo){
            case CASA -> {
                i.mueble(3,3,"CAMA","Una cama para descansar.");
                i.mueble(9,3,"MESA","Una mesa familiar.");
                i.mueble(2,8,"ESTANTERIA","Una estantería de la casa.");
                i.mueble(14,5,"MACETA","Una planta decorativa.");
            }
            case TIENDA -> {
                i.mueble(5,3,"MOSTRADOR","Mostrador de la tienda. Pronto habrá un dependiente.");
                i.mueble(2,4,"ESTANTERIA_GRANDE","Productos colocados en la estantería.");
                i.mueble(11,4,"ESTANTERIA_PEQUENA","Productos para entrenadores.");
                i.mueble(14,8,"VITRINA","Una vitrina con artículos de aventura.");
            }
            case CENTRO_POKEMON -> {
                i.mueble(6,3,"MOSTRADOR","Mostrador de curación. Aún falta la enfermera.");
                i.mueble(12,3,"MAQUINA_CENTRO","Una máquina del Centro Pokémon.");
                i.mueble(2,3,"MAQUINA_AZUL","Equipamiento de asistencia Pokémon.");
                i.mueble(14,8,"TERMINAL","Terminal del Centro Pokémon.");
                i.mueble(2,8,"MACETA","Planta ornamental.");
            }
        }
        if(!i.salidaAlcanzable())
            throw new IllegalStateException("La puerta del interior ha quedado bloqueada: "+tipo);
        return i;
    }

    /**
     * Se abre como diálogo modal desde la vista previa del pueblo. Al salir
     * se devuelve el foco a la ventana principal, sin perder la posición.
     */
    public static void visitar(JFrame padre,AtlasUsuario16 atlas,Tipo tipo,Runnable volver){
        visitar(padre,atlas,tipo,volver,null);
    }

    /**
     * Un servicio real puede asociarse a los muebles sin sustituir sus sprites.
     * La acción se ejecuta solo al pulsar E, Espacio o el botón interactuar.
     */
    public static void visitar(JFrame padre,AtlasUsuario16 atlas,Tipo tipo,Runnable volver,
                                java.util.function.Consumer<Mueble> servicio){
        Interior i=generar(tipo,atlas);
        JDialog ventana=new JDialog(padre,"Interior · "+tipo.name().replace('_',' '),
                Dialog.ModalityType.DOCUMENT_MODAL);
        ventana.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ventana.setLayout(new BorderLayout());
        JLabel estado=new JLabel(" Camina con WASD/flechas · E para inspeccionar · Esc para salir");
        estado.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));
        ventana.add(estado,BorderLayout.NORTH);
        JPanel lienzo=new JPanel(){
            int px=ANCHO/2,py=ALTO-2;
            {
                setFocusable(true);
                setPreferredSize(new Dimension(ANCHO*TILE*ESCALA,ALTO*TILE*ESCALA));
                javax.swing.InputMap teclas=getInputMap(WHEN_IN_FOCUSED_WINDOW);
                for(int[] tecla:new int[][]{
                        {KeyEvent.VK_W,0,-1},{KeyEvent.VK_UP,0,-1},
                        {KeyEvent.VK_S,0,1},{KeyEvent.VK_DOWN,0,1},
                        {KeyEvent.VK_A,-1,0},{KeyEvent.VK_LEFT,-1,0},
                        {KeyEvent.VK_D,1,0},{KeyEvent.VK_RIGHT,1,0}}){
                    String accion="mover"+tecla[0];
                    teclas.put(KeyStroke.getKeyStroke(tecla[0],0),accion);
                    getActionMap().put(accion,new AbstractAction(){
                        @Override public void actionPerformed(ActionEvent e){
                            int nx=px+tecla[1],ny=py+tecla[2];
                            if(!i.puedePisar(nx,ny))return;
                            px=nx;py=ny;repaint();
                            if(i.esSalida(px,py))ventana.dispose();
                            else estado.setText(" "+tipo.name().replace('_',' ')
                                    +" · E: inspeccionar · Esc: salir");
                        }
                    });
                }
                teclas.put(KeyStroke.getKeyStroke(KeyEvent.VK_E,0),"interactuar");
                teclas.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE,0),"interactuar");
                getActionMap().put("interactuar",new AbstractAction(){
                    @Override public void actionPerformed(ActionEvent e){
                        Mueble mueble=i.muebleCercano(px,py);
                        estado.setText(mueble==null?" Nada de interés cerca.":" "+mueble.descripcion());
                        if(mueble!=null && servicio!=null) {
                            servicio.accept(mueble);
                            SwingUtilities.invokeLater(() -> lienzo.requestFocusInWindow());
                        }
                    }
                });
                teclas.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"salir");
                getActionMap().put("salir",new AbstractAction(){
                    @Override public void actionPerformed(ActionEvent e){ventana.dispose();}
                });
            }
            @Override protected void paintComponent(Graphics original){
                super.paintComponent(original);
                Graphics2D g=(Graphics2D)original.create();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                int tam=TILE*ESCALA;
                for(int y=0;y<ALTO;y++)for(int x=0;x<ANCHO;x++){
                    BufferedImage base=atlas.spriteInterior(i.suelo(x,y));
                    g.drawImage(base,x*tam,y*tam,tam,tam,null);
                    if(i.pared(x,y)!=null)
                        g.drawImage(atlas.spriteInterior(i.pared(x,y)),x*tam,y*tam,tam,tam,null);
                }
                for(Mueble m:i.muebles()){
                    BufferedImage sprite=atlas.spriteInterior(m.sprite());
                    g.drawImage(sprite,m.x()*tam,m.y()*tam,
                            sprite.getWidth()*ESCALA,sprite.getHeight()*ESCALA,null);
                }
                // Cursor de posición UI, sin inventar un sprite de protagonista.
                g.setColor(new Color(252,218,79));
                g.drawRect(px*tam+ESCALA,py*tam+ESCALA,
                        tam-2*ESCALA,tam-2*ESCALA);
                g.dispose();
            }
        };
        ventana.add(lienzo,BorderLayout.CENTER);
        JPanel acciones=new JPanel(new GridLayout(1,2,8,0));
        JButton interactuar=new JButton("Interactuar (E)");
        JButton salir=new JButton("Salir al exterior (Esc)");
        interactuar.addActionListener(e -> lienzo.getActionMap().get("interactuar")
                .actionPerformed(new ActionEvent(lienzo,ActionEvent.ACTION_PERFORMED,"interactuar")));
        salir.addActionListener(e -> ventana.dispose());
        acciones.add(interactuar);
        acciones.add(salir);
        ventana.add(acciones,BorderLayout.SOUTH);
        ventana.pack();
        ventana.setLocationRelativeTo(padre);
        ventana.addWindowListener(new java.awt.event.WindowAdapter(){
            @Override public void windowClosed(java.awt.event.WindowEvent e){
                if(volver!=null)volver.run();
            }
        });
        ventana.setVisible(true);
    }
}
