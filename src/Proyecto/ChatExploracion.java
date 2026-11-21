package Proyecto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** T abre escritura, Enter envia el mensaje, Esc cancela. */
public final class ChatExploracion extends JPanel {
    private final JTextArea historial = new JTextArea(6, 30);
    private final JTextField entrada = new JTextField();
    private final JComponent focoMapa;
    private final Timer actualizacion;
    private int mensajes;

    public ChatExploracion(JComponent mapa) {
        focoMapa = mapa;
        setLayout(new BorderLayout(3, 5));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(68,105,142),2),
                BorderFactory.createEmptyBorder(5,7,5,7)));
        setBackground(new Color(27,43,68));
        setPreferredSize(new Dimension(295,195));
        JLabel titulo = new JLabel("CHAT DEL MUNDO · T para escribir");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif",Font.BOLD,12));
        add(titulo,BorderLayout.NORTH);
        historial.setEditable(false);
        historial.setLineWrap(true);
        historial.setWrapStyleWord(true);
        historial.setBackground(new Color(19,32,50));
        historial.setForeground(new Color(237,243,247));
        historial.setFont(new Font("Monospaced",Font.PLAIN,12));
        add(new JScrollPane(historial),BorderLayout.CENTER);
        entrada.setToolTipText("Escribe y pulsa Enter para enviar; Esc para cancelar");
        entrada.setDocument(new javax.swing.text.PlainDocument() {
            @Override public void insertString(int offset,String str,
                    javax.swing.text.AttributeSet a) throws javax.swing.text.BadLocationException {
                if(str==null)return;
                int disponible=240-getLength();
                if(disponible>0)super.insertString(offset,
                        str.substring(0,Math.min(disponible,str.length())),a);
            }
        });
        entrada.setVisible(false);
        entrada.addActionListener(e->enviar());
        entrada.getInputMap(JComponent.WHEN_FOCUSED).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE,0),"cancelar-chat");
        entrada.getActionMap().put("cancelar-chat",new AbstractAction(){
            @Override public void actionPerformed(ActionEvent e){cerrarEntrada();}
        });
        add(entrada,BorderLayout.SOUTH);
        setVisible(false);
        mapa.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke(KeyEvent.VK_T,0),"chat-abrir");
        mapa.getActionMap().put("chat-abrir",new AbstractAction(){
            @Override public void actionPerformed(ActionEvent e){
                if(!entrada.isFocusOwner())abrirEntrada();
            }
        });
        actualizacion=new Timer(180,e->recibir());
        actualizacion.setCoalesce(true);
    }

    @Override public void addNotify(){
        super.addNotify();
        actualizacion.start();
    }
    @Override public void removeNotify(){
        actualizacion.stop();
        super.removeNotify();
    }
    public void abrirEntrada(){
        if(!SwingUtilities.isEventDispatchThread()){
            SwingUtilities.invokeLater(this::abrirEntrada);
            return;
        }
        setVisible(true);
        entrada.setVisible(true);
        revalidate();
        entrada.requestFocusInWindow();
    }
    private void enviar(){
        String texto=RedLAN.validarChat(entrada.getText());
        if(texto!=null){
            RedLAN red=RedLAN.actual();
            if(red==null||!red.estaConectada())
                escribir("Sistema: Conéctate primero a una partida LAN o VPN.");
            else red.enviarChat(texto);
        }
        cerrarEntrada();
    }
    private void cerrarEntrada(){
        entrada.setText("");
        entrada.setVisible(false);
        revalidate();
        focoMapa.requestFocusInWindow();
    }
    private void recibir(){
        RedLAN red=RedLAN.actual();
        if(red==null)return;
        List<String> nuevos=red.consumirChat();
        for(String mensaje:nuevos)escribir(mensaje);
    }
    private void escribir(String texto){
        historial.append("["+LocalTime.now().format(
                DateTimeFormatter.ofPattern("HH:mm"))+"] "+texto+"\n");
        if(++mensajes>=50||historial.getDocument().getLength()>5000){
            String todo=historial.getText();
            historial.setText(todo.substring(Math.max(0,todo.length()-3500)));
            mensajes=0;
        }
        historial.setCaretPosition(historial.getDocument().getLength());
        setVisible(true);
    }
}
