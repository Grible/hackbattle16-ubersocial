If you want to test locally, you have to add the following line to your /etc/hosts file:

	127.0.0.1       isdrivingtest.me steven.isdrivingtest.me


Frontend stuff (also build files) are in git. Only when you want to change it go to `/frontend` and:

	npm install
	bower install
	gulp

Bower components are stored in `/public/components` automatically. You can reference them from there if needed in HTML.
