# wenanceChallenge

Wenance Challenge - Solución propuesta
--------------------------------------

La solución cuenta con dos métodos REST

- GET /bitcoin/prices

	Parámetros: timestamp (formato yyyy-MM-ddTHH:mm:ss, permitiendo agregar milisegundos)

	Devuelve un objeto BitcoinPrice en la fecha más cercana al parámetro enviado, y un código 404 si para la fecha 
	aun no se estaban registrando precios.

	Ejemplo para ejecución local
	http://localhost:8080/bitcoin/prices?timestamp=2022-04-02T17:59:01

- GET /bitcoin/trends

	Parámetros: dateFrom, dateTo (formato yyyy-MM-ddTHH:mm:ss, permitiendo agregar milisegundos)

	Devuelve un objeto BitcoinStats para el rango definido, y un codigo 404 si para el rango aún no se estaban 
	registrando precios.

	Ejemplo para ejecución local
	http://localhost:8080/bitcoin/trends?dateFrom=2022-04-02T17:57:32&dateTo=2022-04-02T17:59:00


**CARGA DE DATOS**

Se creó un proceso scheduled que consulta la cotización del bitcoin con una frecuencia de 10 segundos.


**ESTRUCTURAS DE DATOS**

. _BitcoinPrice_ - Este objeto guarda los mismos valores que son recibidos desde el servicio last_price. Se agrega
un timestamp para identificar la fecha del request.

. _BitcoinStats_ - Este objeto guarda un rango de fechas, más el máximo precio de bitcoin registrado, más valores 
para precio promedio dentro del rango de fechas, y variacion porcentual entre el promedio y el máximo precio 
registrado.

. _BitcoinHistory_ - Estructura de datos que contiene una lista de objetos BitcoinPrice
Para el guardado de datos en memoria se utiliza una linked list, que frente a otras listas es más eficiente
en la insercion de datos, adaptandose a los requerimientos del servicio.


