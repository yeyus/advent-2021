#!/usr/bin/env php
<?php
	$f = fopen( 'php://stdin', 'r' );

	$positions = NULL;
	while( $line = fgets( $f ) ) {
		$positions = array_map('intval', explode(",", $line));
	}

  sort($positions);

	$median = 0;
	$middle = floor(count($positions)/2);
	if ($positions % 2 == 0) {
		$median = floor($positions[$middle] + $positions[$middle + 1]) / 2;
	} else {
		$median = $positions[$middle];
	}

	printf("The median is %d \n", $median);

	$fuel = array_map(function($crab) use ($median) {
		return abs($crab - $median);
	}, $positions);

	printf("Total consumption is %d\n", array_sum($fuel));

	fclose( $f );
?>
