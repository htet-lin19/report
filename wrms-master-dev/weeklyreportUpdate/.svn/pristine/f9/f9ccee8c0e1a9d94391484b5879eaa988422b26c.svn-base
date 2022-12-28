
		$(":file").filestyle({
			placeholder : "No file"
		});
		$(function() {
			// here the code for text minimiser and maxmiser by faisal khan
			var minimized_elements = $('p.text-viewer');
			minimized_elements
					.each(function() {
						var t = $(this).text();
						if (t.length < 12)
							return;
						$(this)
								.html(
										t.slice(0, 12)
												+ '<span>... </span><a href="#" class="more"> more>> </a>'
												+ '<span style="display:none;">'
												+ t.slice(12, t.length)
												+ ' <a href="#" class="less"> << less </a></span>');
					});
			$('a.more', minimized_elements).click(function(event) {
				event.preventDefault();
				$(this).hide().prev().hide();
				$(this).next().show();
			});
			$('a.less', minimized_elements).click(function(event) {
				event.preventDefault();
				$(this).parent().hide().prev().show().prev().show();
			});
		});
	