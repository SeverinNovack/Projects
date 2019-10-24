<?php // UTF-8 marker äöüÄÖÜß€
/**
 * Class PageTemplate for the exercises of the EWA lecture
 * Demonstrates use of PHP including class and OO.
 * Implements Zend coding standards.
 * Generate documentation with Doxygen or phpdoc
 *
 * PHP Version 5
 *
 * @category File
 * @package  Pizzaservice
 * @author   Bernhard Kreling, <b.kreling@fbi.h-da.de>
 * @author   Ralf Hahn, <ralf.hahn@h-da.de>
 * @license  http://www.h-da.de  none
 * @Release  1.2
 * @link     http://www.fbi.h-da.de
 */

// to do: change name 'PageTemplate' throughout this file
require_once './Page.php';

/**
 * This is a template for top level classes, which represent
 * a complete web page and which are called directly by the user.
 * Usually there will only be a single instance of such a class.
 * The name of the template is supposed
 * to be replaced by the name of the specific HTML page e.g. baker.
 * The order of methods might correspond to the order of thinking
 * during implementation.
 * @author   Bernhard Kreling, <b.kreling@fbi.h-da.de>
 * @author   Ralf Hahn, <ralf.hahn@h-da.de>
 */
class Baecker extends Page
{
    // to do: declare reference variables for members 
    // representing substructures/blocks

    /**
     * Instantiates members (to be defined above).
     * Calls the constructor of the parent i.e. page class.
     * So the database connection is established.
     *
     * @return none
     */
    protected function __construct()
    {
        parent::__construct();
        // to do: instantiate members representing substructures/blocks
    }

    /**
     * Cleans up what ever is needed.
     * Calls the destructor of the parent i.e. page class.
     * So the database connection is closed.
     *
     * @return none
     */
    protected function __destruct()
    {
        parent::__destruct();
    }

    /**
     * Fetch all data that is necessary for later output.
     * Data is stored in an easily accessible way e.g. as associative array.
     *
     * @return none
     */
    protected function getViewData()
    {
        $sql = "SELECT bestellung.BestellungsID FROM bestellung";
        $orders = $this->_database->query($sql);
        $i = 0;
        while ($order = $orders->fetch_assoc()) {
            $sql = "SELECT COUNT(*) as best FROM bestelltepizza
                    JOIN bestellung ON bestelltepizza.BID = bestellung.BestellungsID
                    WHERE bestellung.BestellungsID = " . $order['BestellungsID'] . " AND
                    (bestelltepizza.Status = 'bestellt' OR bestelltepizza.Status = 'im Ofen')";
            $amount = $this->_database->query($sql);
            $count = $amount->fetch_assoc();
            if ($count['best'] > 0) {
                $sql = "SELECT pizza.PizzaName, bestelltepizza.Status, bestelltepizza.PizzaID FROM `bestelltepizza` 
                        JOIN `pizza` ON pizza.PizzaNummer = bestelltepizza.PizzaNr
                        JOIN `bestellung` on bestellung.BestellungsID = bestelltepizza.BID
                        WHERE bestelltepizza.BID = " . $order['BestellungsID'];
                $pizzas = $this->_database->query($sql);
                while ($pizza = $pizzas->fetch_assoc()) {
                    $status = $pizza['Status'];
                    $name = utf8_encode($pizza['PizzaName']);
                    $opt1 = "";
                    $opt2 = "";
                    $opt3 = "";
                    ++$i;
                    $cat = "p" . $i;
                    $id = $pizza['PizzaID'];
                    $idname = $cat . "id";
                    if ($status == "bestellt") {
                        $opt1 = "checked";
                    } else if ($status == "im Ofen") {
                        $opt2 = "checked";
                    } else if ($status == "fertig") {
                        $opt3 = "checked";
                    }
                    echo <<<EOT
                            <div class="div-table-row">
                           <div class="table-name-column"> <span>$name</span> </div>
                           <div class="table-column"> <input type="radio" value="bestellt" name="$cat" $opt1 onclick="this.form.submit()"> </div>
                           <div class="table-column"> <input type="radio" value="im Ofen" name="$cat" $opt2 onclick="this.form.submit()"> </div>
                           <div class="table-column"> <input type="radio" value="fertig" name="$cat" $opt3 onclick="this.form.submit()"> </div>
                           <div class="table-column"> <input type="hidden" value="$id" name="$idname"> <br> </div>
                        </div>
                        
EOT;
                }
            }
        }
        // to do: fetch data for this view from the database
    }

    /**
     * First the necessary data is fetched and then the HTML is
     * assembled for output. i.e. the header is generated, the content
     * of the page ("view") is inserted and -if avaialable- the content of
     * all views contained is generated.
     * Finally the footer is added.
     *
     * @return none
     */
    protected function generateView()
    {

        $this->generatePageHeader('Bäcker');
        $config = '<!DOCTYPE html>';
        echo <<<EOT
		<!DOCTYPE html>
		<html lang="de">  
		<head>
			<meta charset="UTF-8"/> 
			<meta http-equiv="refresh" content="5"/>
			<!-- für später: CSS include -->
			<!-- <link rel="stylesheet" href="XXX.css"/> -->
			<meta name="viewport" content="width=device-width, initial-scale=1.0" >
			<link rel="stylesheet" href="BaeckerCSS.css"/>
			<!-- für später: JavaScript include -->
			<!-- <script src="XXX.js"></script> -->
			<title>Bäcker</title>
		</head>
		<body>

			<h1><span style="color:navajowhite">Bestellte Pizzen</span></h1>
			<form class="form-horizontal" method="post" action="baecker.php" target="_self" >
			<div class="div-table-parent">
			<div class="div-table-first-row">
				<div class="table-first-name-column">	<span>Pizza</span> </div>
				<div class="table-first-column">	<span>bestellt</span> </div>
				<div class="table-first-column">	<span>im Ofen</span> </div>
				<div class="table-first-column">	<span>fertig</span> </div>
			</div>
			<br> <br>
			<div class="div-table" style="overflow-y: scroll">
			
				
EOT;
        $this->getViewData();
        echo <<<EOT
        </div>
        </div>
			</form>
		</body>
		
		</html>
EOT;
        // to do: call generateView() for all members
        // to do: output view of this page
        $this->generatePageFooter();
    }

    /**
     * Processes the data that comes via GET or POST i.e. CGI.
     * If this page is supposed to do something with submitted
     * data do it here.
     * If the page contains blocks, delegate processing of the
     * respective subsets of data to them.
     *
     * @return none
     */
    protected function processReceivedData()
    {
        parent::processReceivedData();
        $i = 1;
        while (true) {
            $string = "p" . $i;
            $idstring = $string . "id";
            if (isset($_POST[$string]) && !empty($_POST[$string])) {
                $id = $_POST[$idstring];
                $status = $_POST[$string];
                $sql = "UPDATE `bestelltepizza` SET `Status`='" . $status . "' WHERE PizzaID = $id";
                $this->_database->query($sql);
                ++$i;
            } else break;
        }

        // to do: call processReceivedData() for all members
    }

    /**
     * This main-function has the only purpose to create an instance
     * of the class and to get all the things going.
     * I.e. the operations of the class are called to produce
     * the output of the HTML-file.
     * The name "main" is no keyword for php. It is just used to
     * indicate that function as the central starting point.
     * To make it simpler this is a static function. That is you can simply
     * call it without first creating an instance of the class.
     *
     * @return none
     */
    public static function main()
    {
        try {
            $page = new Baecker();
            $page->processReceivedData();
            $page->generateView();
        } catch (Exception $e) {
            header("Content-type: text/plain; charset=UTF-8");
            echo $e->getMessage();
        }
    }
}

// This call is starting the creation of the page. 
// That is input is processed and output is created.
Baecker::main();

// Zend standard does not like closing php-tag!
// PHP doesn't require the closing tag (it is assumed when the file ends). 
// Not specifying the closing ? >  helps to prevent accidents 
// like additional whitespace which will cause session 
// initialization to fail ("headers already sent"). 
//? >