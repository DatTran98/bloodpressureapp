<?php 
	// DEFINE('DB_USER', 'b31_27554320');
	// DEFINE('DB_PASSWORD', '123456dAt@');
	// DEFINE('DB_HOST', 'sql113.byethost31.com');
	// DEFINE('DB_NAME', 'b31_27554320_datn');
	DEFINE('DB_USER', 'root');
	DEFINE('DB_PASSWORD', '');
	DEFINE('DB_HOST', '127.0.0.1');
	DEFINE('DB_NAME', 'datn');
	$mysqli = @mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_NAME);
	mysqli_set_charset($mysqli,"UTF8");
?>