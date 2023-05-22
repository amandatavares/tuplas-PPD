package projeto;

import net.jini.space.JavaSpace;
import net.jini.core.lease.Lease;

import java.util.*;

public class SpaceManager {

    public static List<AmbTupla> listAmb(JavaSpace space) throws Exception {
        List<AmbTupla> listaAmb = new ArrayList<>();
        AmbTupla template = new AmbTupla();
        AmbTupla amb;

        do {
            amb = (AmbTupla) space.take(template, null, JavaSpace.NO_WAIT);

            if (amb != null) {
                listaAmb.add(amb);
            }
        } while (amb != null);

        for (int i = 0; i < listaAmb.size(); i++) {
            amb = listaAmb.get(i);
            space.write(amb, null, Lease.FOREVER);
        }

        return listaAmb;
    }

    public static List<DispTupla> listDisp(JavaSpace space, String amb) throws Exception {
        List<DispTupla> listaDisp = new ArrayList<>();
        DispTupla template = new DispTupla();
        DispTupla disp;

        if (!"".equals(amb)) {
            template.amb = amb;
        }

        do {
            disp = (DispTupla) space.take(template, null, JavaSpace.NO_WAIT);

            if (disp != null) {
                listaDisp.add(disp);
            }
        } while (disp != null);

        for (int i = listaDisp.size() - 1; i >= 0; i--) {
            disp = listaDisp.get(i);
            space.write(disp, null, Lease.FOREVER);

            if (amb == null && disp.amb != null) {
                listaDisp.remove(disp);
            }
        }

        return listaDisp;
    }

    public static List<UserTupla> listUser(JavaSpace space, String amb) throws Exception {
        List<UserTupla> listaUser = new ArrayList<>();
        UserTupla template = new UserTupla();
        UserTupla user;

        if (!"".equals(amb)) {
            template.amb = amb;
        }

        do {
            user = (UserTupla) space.take(template, null, JavaSpace.NO_WAIT);

            if (user != null) {
                listaUser.add(user);
            }
        } while (user != null);

        for (int i = listaUser.size() - 1; i >= 0; i--) {
            user = listaUser.get(i);
            space.write(user, null, Lease.FOREVER);

            if (amb == null && user.amb != null) {
                listaUser.remove(user);
            }
        }

        return listaUser;
    }

    public static UserTupla findUser(JavaSpace space, String nomeUsuario) throws Exception {
        UserTupla template = new UserTupla();
        template.name = nomeUsuario;
        return (UserTupla) space.read(template, null, JavaSpace.NO_WAIT);

    }

}
