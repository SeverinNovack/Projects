<?php	// UTF-8 marker äöüÄÖÜß€
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
class Kunde extends Page
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
        if(isset($_SESSION['BID'])) {
            $sql = "SELECT pizza.PizzaName, bestelltepizza.Status FROM `bestelltepizza` 
                    JOIN `pizza` ON pizza.PizzaNummer = bestelltepizza.PizzaNr WHERE bestelltepizza.BID = " . $_SESSION['BID'];
            $result = $this->_database->query($sql);
            while($pizza = $result->fetch_assoc()) {
                $status = $pizza['Status'];
                $name = utf8_encode($pizza['PizzaName']);
                $string = $name . ": " . $status;
                //<span>$string</span> <br>
                echo <<<EOT
                <section id="ordstat">  
                </section>

EOT;
             }
        // to do: fetch data for this view from the database
        }
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
        $this->generatePageHeader('Kunde');
        $config = '<!DOCTYPE html>';
        //$string = '[{"PizzaName":"Schinken","Status":"bestellt"},{"PizzaName":"Margherita","Status":"bestellt"}]';
        $jstring = '[{"PizzaName":"Schinken","Status":"bestellt"},{"PizzaName":"Margherita","Status":"bestellt"}]';
        $string = "[{'PizzaName':'Schinken','Status':'bestellt'},{'PizzaName':'Margherita','Status':'bestellt'}]";
        $json = json_encode($string);
		echo <<<EOT
		<!DOCTYPE html>
		<html lang="de">  
		<head>
			<meta charset="UTF-8" />
			<!-- für später: CSS include -->
			<link rel="stylesheet" href="KundeCSS.css"/> 
			<!-- für später: JavaScript include -->
			<!-- <script src="XXX.js"></script> -->
			
			<title>Kunde</title>
		</head>
		<!-- <body onload="process([{'PizzaName':'Schinken','Status':'bestellt'},{'PizzaName':'Margherita','Status':'bestellt'}])"> -->
		    <body onload="requestData()">
		    <script src="StatusUpdate.js"></script>
                <section id="ordstat">   
                <h1>Lieferstatus</h1>
                </section>
EOT;
		//$this->getViewData();

    echo <<<EOT
				<br>
				<br>
				<form class="form-horizontal" method="post" action="bestellung.php" target="_self">
					<div class="newOrderButton"><button type="submit" tabindex="0" accesskey="n">Neue Bestellung</button></div>
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

        if (isset($_POST["adresse"]) && !empty($_POST["adresse"])) {
            $adresse = htmlspecialchars(mysqli_real_escape_string($this->_database, $_POST['adresse']));
            $sql = "INSERT INTO bestellung(Adresse) VALUES ('" . $adresse . "')";
            if (isset($_POST["pizza"]) && !empty($_POST["pizza"])) {
                $this->_database->query($sql);
                $bid = mysqli_insert_id($this->_database);
                $_SESSION['BID'] = $this->_database->insert_id;
                $selectedValues = (array)$_POST['pizza'];
                foreach ($selectedValues as $value) {
                    $sql = "INSERT INTO bestelltepizza(BID, PizzaNr) VALUES (" . $bid . ", '" . $value . "')";
                    $this->_database->query($sql);
                }
                header('Location: kunde.php');
                exit;
            }
        }


        // to do: call processReceivedData() for all members
    }

    /**x
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
            session_start();
            $page = new Kunde();
            $page->processReceivedData();
            $page->generateView();
        }
        catch (Exception $e) {
            header("Content-type: text/plain; charset=UTF-8");
            echo $e->getMessage();
        }
    }
}

// This call is starting the creation of the page. 
// That is input is processed and output is created.
Kunde::main();

// Zend standard does not like closing php-tag!
// PHP doesn't require the closing tag (it is assumed when the file ends). 
// Not specifying the closing ? >  helps to prevent accidents 
// like additional whitespace which will cause session 
// initialization to fail ("headers already sent"). 
//? >