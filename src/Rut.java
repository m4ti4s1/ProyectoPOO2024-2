import java.util.Objects;

public class Rut implements IdPersona {
    private int numero;
    private char dv;

    public Rut(int num, char dv) {
        this.numero = num;
        this.dv = dv;
    }

    @Override
    public String toString() {
        String num=""+numero;
        char [] Arraynumero=num.toCharArray();
        if(Arraynumero.length==8) {
            return "Rut{ " + Arraynumero[0] + Arraynumero[1] + '.' + Arraynumero[2] +
                    Arraynumero[3] + Arraynumero[4] + '.' + Arraynumero[5]
                    + Arraynumero[6] + Arraynumero[7] + '-' + dv + '}';
        }
        return "Rut{ " + Arraynumero[0]  + '.' + Arraynumero[1] +
                Arraynumero[2] + Arraynumero[3] + '.' + Arraynumero[4]
                + Arraynumero[5] + Arraynumero[6] + '-' + dv + '}';



    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Rut rut)) return false;
        return numero == rut.numero && dv == rut.dv;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numero, dv);
    }

    public int getNumero() {
        return numero;
    }

    public char getDv() {
        return dv;
    }
    public static Rut of(String rutConDv){
        char [] toChar=rutConDv.toCharArray();
        int posicion=0;
        for(int i=0;i<toChar.length;i++){
            if (toChar[i]==('-')){
                posicion=i;
            }
        }
        String rut="";
        for(int i=0;i<posicion;i++){
            rut+=toChar[i];

        }

        int rutNumero = Integer.parseInt(rut);

        return new Rut(rutNumero,toChar[posicion+1]);

    }
}

