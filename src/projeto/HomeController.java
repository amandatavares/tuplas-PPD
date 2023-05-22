package projeto;
import java.rmi.RemoteException;
import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.jini.core.entry.UnusableEntryException;
import net.jini.core.transaction.TransactionException;

/**
 *
 * @author Amanda
 */
public class HomeController {

    private JavaSpace space;
    public HomeController() {
        try {
            System.out.println("Procurando pelo servico JavaSpace...");
            Lookup finder = new Lookup(JavaSpace.class);
            this.space = (JavaSpace) finder.getService();
            if (space == null) {
                System.out.println("O servico JavaSpace nao foi encontrado. Encerrando...");
                System.exit(-1);
            }
            System.out.println("O servico JavaSpace foi encontrado.");
        } catch (Exception e) {
            System.exit(-1);
        }
    }
    
    //Ambiente
    public String createAmb() {
        String finalName;
        try {
            AmbTupla novoAmb = new AmbTupla();
            int ambIndex = 0;

            while (true) {
                ambIndex += 1;
                String ambName = "amb" + ambIndex;
                novoAmb.name = ambName;
                AmbTupla tempAmb = (AmbTupla) this.space.read(novoAmb, null, JavaSpace.NO_WAIT);

                if (tempAmb == null) {
                    this.space.write(novoAmb, null, Lease.FOREVER);
                    System.out.println("\r\nAmbiente " + ambName + " criado");
                    finalName = ambName;
                    break;
                }

            }
            return finalName;
        } catch (InterruptedException | RemoteException | UnusableEntryException | TransactionException e) {
        }
        return null;
    }
    public void deleteAmb(String oldNome) {
        try {
            AmbTupla oldAmb = new AmbTupla();
            oldAmb.name = oldNome;
            space.take(oldAmb, null, JavaSpace.NO_WAIT);
            System.out.println("\r\nAmbiente " + oldNome + " deletado");

        } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    //Dispositivo
    public String createDisp() {
        String finalName;
        try {
            DispTupla novoDisp = new DispTupla();
            int dispIndex = 0;

            while (true) {
                dispIndex += 1;
                String dispName = "disp" + dispIndex;
                novoDisp.name = dispName;
                DispTupla tempDisp = (DispTupla) space.read(novoDisp, null, JavaSpace.NO_WAIT);

                if (tempDisp == null) {
                    space.write(novoDisp, null, Lease.FOREVER);
                    System.out.println("\r\nDispositivo " + dispName + " criado");
                    finalName = dispName;
                    break;
                }
            }
            return finalName;
        } catch (InterruptedException | RemoteException | UnusableEntryException | TransactionException e) {
        }
        return null;
    }
    public void deleteDisp(String oldNome) {
        try {
            DispTupla oldDisp = new DispTupla();
            oldDisp.name = oldNome;
            space.take(oldDisp, null, JavaSpace.NO_WAIT);
            System.out.println("\r\nDispositivo " + oldNome + " deletado");

        } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void moveDisp(String nome, String novoAmb) {
        try {
            DispTupla oldDisp = new DispTupla();
            oldDisp.name = nome;
            DispTupla disp = (DispTupla) space.take(oldDisp, null, JavaSpace.NO_WAIT);

            disp.amb = novoAmb;
            space.write(disp, null, Lease.FOREVER);
            System.out.println("\r\nDispositivo " + nome + " movido para o ambiente " + novoAmb);

        } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public String listDisp(String ambNome) {
        String nomesDispositivos = "";
        String nomesDispositivosNull = " Sem dispositivos. ";
        try {
            List<DispTupla> listaDisp = SpaceManager.listDisp(space, ambNome);
            for (int i = 0; i < listaDisp.size(); i++) {
                nomesDispositivos += listaDisp.get(i).name;
                if (i < listaDisp.size() - 1) {
                    nomesDispositivos += ", ";
                }
            }
            System.out.println(nomesDispositivos);
            nomesDispositivos = "DISP: \n" + nomesDispositivos + "\n";

            if (listaDisp.isEmpty()) {
                nomesDispositivos = nomesDispositivosNull;
            }

            listaDisp.clear();
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nomesDispositivos;
    }
    public int countDisp(String ambNome) {
        try {
            List<DispTupla> listaDisp = SpaceManager.listDisp(space, ambNome);
            return listaDisp.size();
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
   
    
    //Usuário
    public String createUser() {
        String finalName;
        try {
            UserTupla usuario = new UserTupla();
            int userIndex = 0;

            while (true) {
                userIndex += 1;
                String userName = "user" + userIndex;
                usuario.name = userName;
                UserTupla tempUser = (UserTupla) space.read(usuario, null, JavaSpace.NO_WAIT);

                if (tempUser == null) {
                    space.write(usuario, null, Lease.FOREVER);
                    System.out.println("\r\nUsuario registrado como: " + usuario.name);
                    finalName = userName;
                    break;
                }
            }
            return finalName;
        } catch (InterruptedException | RemoteException | UnusableEntryException | TransactionException e) {
        }
        return null;
    }
    public void deleteUser(String oldNome) {
        try {
            UserTupla oldUser = new UserTupla();
            oldUser.name = oldNome;
            space.take(oldUser, null, JavaSpace.NO_WAIT);
            System.out.println("\r\nUsuario " + oldUser + " destruido");

        } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public void moveUser(String nome, String novoAmb) {
        try {
            UserTupla oldUser = new UserTupla();
            oldUser.name = nome;
            UserTupla usuario = (UserTupla) space.take(oldUser, null, JavaSpace.NO_WAIT);

            oldUser.amb = novoAmb;
            space.write(oldUser, null, Lease.FOREVER);
            System.out.println("\r\nUsuario " + nome + " movido para o ambiente " + novoAmb);

        } catch (UnusableEntryException | TransactionException | InterruptedException | RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    public String listUser(String ambNome) {
        String nomesUsuarios = "";
        String nomesUsuariosNull = "\n Sem usuários. ";
        try {
            List<UserTupla> listaUser = SpaceManager.listUser(space, ambNome);
            for (int i = 0; i < listaUser.size(); i++) {
                nomesUsuarios += listaUser.get(i).name;
                if (i < listaUser.size() - 1) {
                    nomesUsuarios += ", ";
                }
            }
            System.out.println(nomesUsuarios);
            nomesUsuarios = "\nUSER: \n" + nomesUsuarios + "\n";

            if (listaUser.isEmpty()) {
                nomesUsuarios = nomesUsuariosNull;
            }

            listaUser.clear();
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nomesUsuarios;
    }

    
    //BatePapo
    public String findAmbUserChat(String nomeUsuario) {
        String ambienteNome = "";
        try {
            UserTupla user = SpaceManager.findUser(space, nomeUsuario);
            ambienteNome = user.amb;
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ambienteNome;
    }
    public String listUserAmbChat(String nomeUsuario) {
        String nomesUsuarios = "";
        try {
           
            String ambNome = findAmbUserChat(nomeUsuario);

            List<UserTupla> listaUser = SpaceManager.listUser(space, ambNome);
            for (int i = 0; i < listaUser.size(); i++) {
                nomesUsuarios += listaUser.get(i).name;
                if (i < listaUser.size() - 1) {
                    nomesUsuarios += ", ";
                }
            }
            listaUser.clear();
        } catch (Exception ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return nomesUsuarios;
    }
    public void sendMessage(String nomeTo, String usuario, String ambAtual, String mensagemBP) {
        try {
            MsgTupla novaMsg = new MsgTupla();
            novaMsg.time = System.currentTimeMillis();
            novaMsg.from = usuario;
            novaMsg.to = nomeTo;
            novaMsg.amb = ambAtual;
            novaMsg.msg = mensagemBP;

            space.write(novaMsg, null, Lease.FOREVER);
            System.out.println("\r\nMensagem enviada a " + nomeTo);
        } catch (TransactionException | RemoteException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String receiveMessage(String usuario, String ambAtual) {
        String mensagem = "";
        try {
            MsgTupla msgTemplate = new MsgTupla();
            msgTemplate.to = usuario;
            msgTemplate.amb = ambAtual;

            MsgTupla novaMsg = (MsgTupla) space.take(msgTemplate, null, JavaSpace.NO_WAIT);

            if (novaMsg == null) {
                System.out.println("\r\nNao ha novas mensagens");
            } else {
                System.out.println();

                while (novaMsg != null) {
                    mensagem = novaMsg.from + ": " + novaMsg.msg + "\n";
                    novaMsg = (MsgTupla) space.take(msgTemplate, null, JavaSpace.NO_WAIT);
                    
                }
            }
        } catch (RemoteException | UnusableEntryException | TransactionException | InterruptedException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mensagem;
    }
    
}
