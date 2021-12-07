#!/usr/bin/env php
<?php
	$f = fopen( 'php://stdin', 'r' );

	$positions = NULL;
	while( $line = fgets( $f ) ) {
		$positions = array_map('intval', explode(",", $line));
	}

	sort($positions);

	function fuelSpent($middle, $pos) {
		return array_sum(array_map(function($crab) use ($middle) {
			$n = abs($crab - $middle);
			return $n*($n + 1)/2;
		}, $pos));
	}

	$avg = floor(array_sum($positions) / count($positions));
	printf("Average is %d\n", $avg);
	printf("Cost is %d\n", fuelSpent($avg, $positions));

	/*
	$l = positions[0];
	$r = positions[count($positions) - 1];

	while ($l < $r) {
		$middle = ($r - $l) / 2;

		
	}
	 */

	fclose( $f );
?>
