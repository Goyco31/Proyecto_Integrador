package com.integrador.spring.app.Controlador;

import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import com.integrador.spring.app.Modelo.ComprarMonedas;
import com.integrador.spring.app.Modelo.Juego;
import com.integrador.spring.app.Modelo.Recarga;
import com.integrador.spring.app.Modelo.Recompensa;
import com.integrador.spring.app.Modelo.Torneo;
import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.ComprarMonedasServices;
import com.integrador.spring.app.Servicio.JuegoServices;
import com.integrador.spring.app.Servicio.RecargaServices;
import com.integrador.spring.app.Servicio.RecompensaServices;
import com.integrador.spring.app.Servicio.TorneoServices;
import com.integrador.spring.app.Servicio.UsuarioServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ver/excel")
public class ExportarExcel extends AbstractXlsxView {

    @Autowired
    private UsuarioServices services_user;
    @Autowired
    private TorneoServices services_torneo;
    @Autowired
    private JuegoServices services_juego;
    @Autowired
    private RecompensaServices services_recompensa;
    @Autowired
    private ComprarMonedasServices services_compra;
    @Autowired
    private RecargaServices services_recarga;

    @GetMapping("/usuarios")
    protected void usuariosExcel(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"listado-usuarios.xlsx\"");
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet hoja = wb.createSheet("Usuarios");

        Row filaTitulo = hoja.createRow(0);
        Cell celda = filaTitulo.createCell(0);
        celda.setCellValue("Listado de usuarios");

        Row filaData = hoja.createRow(2);
        String columnas[] = { "ID", "NOMBRE", "APELLIDO", "CORREO", "FECHA DE REGISTRO", "NICKNAME", "MONEDAS", "ROL" };
        for (int i = 0; i < columnas.length; i++) {
            celda = filaData.createCell(i);
            celda.setCellValue(columnas[i]);
        }

        List<User> lista = (List<User>) services_user.listarUsuarios();

        int numFila = 3;
        for (User user : lista) {
            filaData = hoja.createRow(numFila);
            filaData.createCell(0).setCellValue(user.getId());
            filaData.createCell(1).setCellValue(user.getNombre());
            filaData.createCell(2).setCellValue(user.getApellido());
            filaData.createCell(3).setCellValue(user.getCorreo());
            filaData.createCell(4).setCellValue(user.getFecha_registro().toString());
            filaData.createCell(5).setCellValue(user.getNickname());
            filaData.createCell(6).setCellValue(user.getMonedas());
            filaData.createCell(7).setCellValue(user.getRole().toString());
            numFila++;
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        wb.write(response.getOutputStream());
    }

    @GetMapping("/torneos")
    protected void torneosExcel(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"listado-torneos.xlsx\"");
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet hoja = wb.createSheet("Torneos");

        Row filaTitulo = hoja.createRow(0);
        Cell celda = filaTitulo.createCell(0);
        celda.setCellValue("Listado de Torneos");

        Row filaData = hoja.createRow(2);
        String columnas[] = { "ID", "NOMBRE", "DESCRIPCION", "TIPO", "FECHA DE INICIO", "PREMIO", "CUPOS", "FORMATO",
                "REGLAMENTO", "ESTADO", "JUEGO" };
        for (int i = 0; i < columnas.length; i++) {
            celda = filaData.createCell(i);
            celda.setCellValue(columnas[i]);
        }

        List<Torneo> lista = services_torneo.listarTorneo();

        int numFila = 3;
        for (Torneo torneo : lista) {
            filaData = hoja.createRow(numFila);
            filaData.createCell(0).setCellValue(torneo.getIdTorneo());
            filaData.createCell(1).setCellValue(torneo.getNombre());
            filaData.createCell(2).setCellValue(torneo.getDescripcion());
            filaData.createCell(3).setCellValue(torneo.getTipo().toString());
            filaData.createCell(4).setCellValue(torneo.getFecha().toString());
            filaData.createCell(5).setCellValue(torneo.getPremio());
            filaData.createCell(6).setCellValue(torneo.getCupos());
            filaData.createCell(7).setCellValue(torneo.getFormato());
            filaData.createCell(8).setCellValue(torneo.getDocReglamentoBase64());
            filaData.createCell(9).setCellValue(torneo.getEstado().toString());
            filaData.createCell(10).setCellValue(torneo.getJuego().getNombreJuego());
            numFila++;
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        wb.write(response.getOutputStream());
    }

    @GetMapping("/juegos")
    protected void juegosExcel(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"listado-juegos.xlsx\"");
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet hoja = wb.createSheet("Juegos");

        Row filaTitulo = hoja.createRow(0);
        Cell celda = filaTitulo.createCell(0);
        celda.setCellValue("Listado de Juegos");

        Row filaData = hoja.createRow(2);
        String columnas[] = { "ID", "NOMBRE", "GENERO", "IMAGEN" };
        for (int i = 0; i < columnas.length; i++) {
            celda = filaData.createCell(i);
            celda.setCellValue(columnas[i]);
        }

        List<Juego> lista = services_juego.listarJuegos();

        int numFila = 3;
        for (Juego juego : lista) {
            filaData = hoja.createRow(numFila);
            filaData.createCell(0).setCellValue(juego.getIdJuego());
            filaData.createCell(1).setCellValue(juego.getNombreJuego());
            filaData.createCell(2).setCellValue(juego.getGeneroJuego());
            filaData.createCell(3).setCellValue(juego.getImgJuegoBase64());

            numFila++;
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        wb.write(response.getOutputStream());
    }

    @GetMapping("/recompensas")
    protected void recompensasExcel(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"listado-recompensas.xlsx\"");
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet hoja = wb.createSheet("Recompensas");

        Row filaTitulo = hoja.createRow(0);
        Cell celda = filaTitulo.createCell(0);
        celda.setCellValue("Listado de Recompensas");

        Row filaData = hoja.createRow(2);
        String columnas[] = { "ID", "NOMBRE", "DESCRIPCION", "COSTO", "DISPONIBLE", "CANTIDAD", "IMAGEN" };
        for (int i = 0; i < columnas.length; i++) {
            celda = filaData.createCell(i);
            celda.setCellValue(columnas[i]);
        }

        List<Recompensa> lista = services_recompensa.listarTodas();

        int numFila = 3;
        for (Recompensa recompensa : lista) {
            filaData = hoja.createRow(numFila);
            filaData.createCell(0).setCellValue(recompensa.getIdRecompensa());
            filaData.createCell(1).setCellValue(recompensa.getNombre());
            filaData.createCell(2).setCellValue(recompensa.getDescripcion());
            filaData.createCell(3).setCellValue(recompensa.getCosto());
            filaData.createCell(4).setCellValue(recompensa.isDisponible());
            filaData.createCell(5).setCellValue(recompensa.getCantidad());
            filaData.createCell(6).setCellValue(recompensa.getImgRecompensaBase64());

            numFila++;
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        wb.write(response.getOutputStream());
    }

    @GetMapping("/opcionesRecarga")
    protected void opcionesRecargaExcel(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"listado-opcionesRecarga.xlsx\"");
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet hoja = wb.createSheet("Opciones de recarga");

        Row filaTitulo = hoja.createRow(0);
        Cell celda = filaTitulo.createCell(0);
        celda.setCellValue("Listado de Opciones de Recarga");

        Row filaData = hoja.createRow(2);
        String columnas[] = { "ID", "NOMBRE", "CANTIDAD", "PRECIO", "IMAGEN" };
        for (int i = 0; i < columnas.length; i++) {
            celda = filaData.createCell(i);
            celda.setCellValue(columnas[i]);
        }

        List<ComprarMonedas> lista = services_compra.listar();

        int numFila = 3;
        for (ComprarMonedas compra : lista) {
            filaData = hoja.createRow(numFila);
            filaData.createCell(0).setCellValue(compra.getIdCompra());
            filaData.createCell(1).setCellValue(compra.getNombre());
            filaData.createCell(2).setCellValue(compra.getCantidad());
            filaData.createCell(3).setCellValue(compra.getPrecioCompra().toString());
            filaData.createCell(4).setCellValue(compra.getImgimgMonedaBase64());

            numFila++;
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        wb.write(response.getOutputStream());
    }

    @GetMapping("/historialRecarga")
    protected void historialRecargaExcel(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"listado-historialRecarga.xlsx\"");
        XSSFWorkbook wb = new XSSFWorkbook();
        Sheet hoja = wb.createSheet("Historial de recargas");

        Row filaTitulo = hoja.createRow(0);
        Cell celda = filaTitulo.createCell(0);
        celda.setCellValue("Listado de recargas realizadas");

        Row filaData = hoja.createRow(2);
        String columnas[] = { "ID", "ESTADO", "FECHA", "PAGO", "USUARIO", "OPCION DE RECARGA", "CANTIDAD", "PRECIO" };
        for (int i = 0; i < columnas.length; i++) {
            celda = filaData.createCell(i);
            celda.setCellValue(columnas[i]);
        }

        List<Recarga> lista = services_recarga.listarRecarga();

        int numFila = 3;
        for (Recarga recarga : lista) {
            filaData = hoja.createRow(numFila);
            filaData.createCell(0).setCellValue(recarga.getIdRecarga());
            filaData.createCell(1).setCellValue(recarga.getEstado().toString());
            filaData.createCell(2).setCellValue(recarga.getFechaRecarga().toString());
            filaData.createCell(3).setCellValue(recarga.getTipoPago().toString());
            filaData.createCell(4).setCellValue(recarga.getUsuario().getNickname());
            filaData.createCell(5).setCellValue(recarga.getComprarMonedas().getNombre());
            filaData.createCell(6).setCellValue(recarga.getComprarMonedas().getCantidad());
            filaData.createCell(7).setCellValue(recarga.getComprarMonedas().getPrecioCompra().toString());

            numFila++;
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        wb.write(response.getOutputStream());
    }

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buildExcelDocument'");
    }
}
