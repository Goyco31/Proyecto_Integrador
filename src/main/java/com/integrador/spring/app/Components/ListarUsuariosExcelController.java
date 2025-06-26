package com.integrador.spring.app.Components;

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

import com.integrador.spring.app.Modelo.User;
import com.integrador.spring.app.Servicio.UsuarioServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/ver/excel")
public class ListarUsuariosExcelController extends AbstractXlsxView {

    @Autowired
    private UsuarioServices services_user;

    @GetMapping("/usuarios")
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request,
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
            filaData.createCell(4).setCellValue(user.getFecha_registro());
            filaData.createCell(5).setCellValue(user.getNickname());
            filaData.createCell(6).setCellValue(user.getMonedas());
            filaData.createCell(7).setCellValue(user.getRole().toString());
            numFila++;
        }
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        wb.write(response.getOutputStream());
    }

}
