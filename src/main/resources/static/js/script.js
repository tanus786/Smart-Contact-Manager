
//Dashboad Sidebar

const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {
		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	}
	else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}

};
//Search Bar

const search = () => {
	/*console.log("Searching")*/
	let query = $("#search-input").val();
	if (query == "") {
		$(".serach-result").hide();
	} else {
		//Search
		console.log(query);
		//Sending Request to server
		let url = `http://localhost:9595/search/${query}`;
		fetch(url)
			.then((response) => {
				return response.json();
			})
			.then((data) => {
				console.log(data);
				let text = `<div class='list-group'>`;

				data.forEach((contact) => {
					text += `<a href='#' class='list-group-item list-group-action'>${contact.name}</a>`;
				});
				text += `</div>`;
				$(".search-result").html(text);
				if (data.length > 0) {
					$(".search-result").show();
				}
			});

	}
};



function toggleResetPswd(e) {
	e.preventDefault();
	$('#logreg-forms .form-signin').toggle() // display:block or none
	$('#logreg-forms .form-reset').toggle() // display:block or none
}

function toggleSignUp(e) {
	e.preventDefault();
	$('#logreg-forms .form-signin').toggle(); // display:block or none
	$('#logreg-forms .form-signup').toggle(); // display:block or none
}

/*$(() => {
	// Login Register Form
	$('#logreg-forms #forgot_pswd').click(toggleResetPswd);
	$('#logreg-forms #cancel_reset').click(toggleResetPswd);
	$('#logreg-forms #btn-signup').click(toggleSignUp);
	$('#logreg-forms #cancel_signup').click(toggleSignUp);
})*/
