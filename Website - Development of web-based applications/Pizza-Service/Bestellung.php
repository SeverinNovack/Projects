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
class Bestellung extends Page
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
        $sql = "SELECT * FROM pizza";
        $result = $this->_database->query($sql);
        $i = 0;
        while ($pizza = $result->fetch_assoc()) {
            $bilddatei = utf8_encode($pizza['Bilddatei']);
            $name = utf8_encode($pizza['PizzaName']);
            $preis = $pizza['Preis'];
            $pnr = $pizza['PizzaNummer'];
            $id = 'p' . $i;
            $pid = 'pr' . $i;
            echo <<<EOT
                    <div class="item">
					<div class="name"><br> <span id="$id">$name</span> <br> </div>
					<div class="price"><span id="$pid">$preis €</span> </div>
					<br>
					<div class="picture"><img src="$bilddatei" alt="" onclick="addToCart($id, $pnr, $preis)"></div>
					<br> <br>
					<hr>
                    </div>
EOT;
            $i++;

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

        $this->generatePageHeader('Bestellung');
        $config = '<!DOCTYPE html>';
        echo <<<EOT
		<!DOCTYPE html>
		<html lang="de">  
		<head>
			<meta charset="UTF-8" />
			<!-- für später: CSS include -->
			<link rel="stylesheet" href="BestellungCSS.css">
		    <script src="pizzascript.js"></script>
			<title>Bestellung</title>
		</head>
		<body>
			
            <header>
			<!--Statisch-->
			 </header>
			  <h1>Bestellung</h1>
			  <div class="wrapper">
			 <section>
			  <div class="card">
				<div class="h2S"><h2>Speisekarte</h2></div> 
				<hr>
EOT;
        $this->getViewData();

        echo <<<EOT
                </div>
			</section>
			<!--Dynamisch-->
			<section>
			    <div class="WarenkorbWrapper">
				<div class="Warenkorb">
				<div class="h2w"><h2>Warenkorb</h2></div>
				<hr>
				<br>
				<br>
				<form class="form-horizontal" method="post" action="kunde.php" target="_blank" onclick="btnActivity()">	
	                <div class="Selectbox">
					<select name="pizza[]" size="4" multiple tabindex="0" id="order">
					</select> </div>
					
					<br>
					<div class="totalprice"><strong id="totalprice">0</strong> <strong> €</strong> </div>
				    <br> 
				
					<div class="adrinput"><input type="text" name="adresse" placeholder="Ihre Adresse" id="adrinput" onkeyup="btnActivity()">
					</div><br>
					<div class="buttons">
					<button type="button" tabindex="0" accesskey="a" name="dltallbtn" onclick="deleteAllSelected()">Alle löschen</button>
					<button type="button" tabindex="0" accesskey="d" name="dltbtn" onclick="deleteSelected()">Auswahl löschen</button>
					<button type="submit" tabindex="0" accesskey="o" name="ordbtn" id="ordbtn" disabled onclick="makeEverythingSelected()">Bestellen</button>
				    </div>
				</form>
				</div>
				</div>
			</section>
	        </div>
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
            session_start();
            $page = new Bestellung();
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
Bestellung::main();

// Zend standard does not like closing php-tag!
// PHP doesn't require the closing tag (it is assumed when the file ends).
// Not specifying the closing ? >  helps to prevent accidents
// like additional whitespace which will cause session
// initialization to fail ("headers already sent").
//? >